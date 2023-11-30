package mx.gob.imss.cit.pmc.cierreanualimss.schedule;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.services.CierreAnualIMSSService;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.NumberUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class BatchCierreAnualIMSSSchedule {

    private static final Logger logger = LoggerFactory.getLogger(BatchCierreAnualIMSSSchedule.class);

    @Autowired
    @Qualifier("jobLauncherSchedule")
    private SimpleJobLauncher jobLauncherSchedule;

    @Autowired
    @Qualifier("cierreAnualIMSSJob")
    private Job cierreAnualIMSSJob;

    @Autowired
    @Qualifier("cierreAnualIMSSNextJob")
    private Job cierreAnualIMSSNextJob;

    @Autowired
    private CierreAnualIMSSService cierreAnualIMSSService;

    @Bean
    public SimpleJobLauncher jobLauncherSchedule(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @Scheduled(cron = "${cierreAnualImssBatchJunio}")
    public void cierreAnualImssBatchJunio() {
        logger.info("Se inicia la ejecucion del proceso");
        ejecucionBatch();
    }
    
    @Scheduled(cron = "${cierreAnualImssBatchEnero}")
    public void cierreAnualImssBatchEnero() {
        logger.info("Se inicia la ejecucion del proceso");
        ejecucionBatch();
    }
    
    @Scheduled(cron = "${cierreAnualImssBatchFebrero}")
    public void cierreAnualImssBatchFebrero() {
        logger.info("Se inicia la ejecucion del proceso");
        ejecucionBatch();
    }

	private void ejecucionBatch() {
		List<Boolean> jobExecutionStatus = new ArrayList<>();
        try {
            for (int del : CierreAnualImssConstants.DEL_SUBDEL.keySet()) {
                JobParameters parameters = new JobParametersBuilder()
                        .addLong(CierreAnualImssConstants.KEY_PARAM_DEL, (long) del)
                        .addDate(CierreAnualImssConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                        .addString(CierreAnualImssConstants.REPROCESS_FLAG, Boolean.FALSE.toString())
                        .toJobParameters();
                JobExecution jobExecution = jobLauncherSchedule.run(cierreAnualIMSSJob, parameters);
                logger.info("Se proceso el archivo de la delegacion {} con estatus {} y key {}",
                        NumberUtils.getDel((long) del),
                        jobExecution.getExitStatus(),
                        del);
                jobExecutionStatus.add(ExitStatus.COMPLETED.getExitCode().equals(jobExecution.getExitStatus().getExitCode()));
            }
            JobParameters parameters = new JobParametersBuilder()
                    .addDate(CierreAnualImssConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                    .addString(CierreAnualImssConstants.NOT_ERROR_FLAG_PARAM, jobExecutionStatus.stream()
                            .reduce(Boolean.TRUE, Boolean::logicalAnd).toString())
                    .addString(CierreAnualImssConstants.REPROCESS_FLAG, Boolean.FALSE.toString())
                    .toJobParameters();
            JobExecution jobExecutionNext = jobLauncherSchedule.run(cierreAnualIMSSNextJob, parameters);
            logger.info("Se realizo el envio de los archivos a los servidores sftp con status {}", jobExecutionNext.getExitStatus());
        } catch (Exception e) {
            logger.error("Error al ejecutar la tarea", e);
        }
	}
	
	@Scheduled(cron = "0 0 6,11 01 JUL ?")
	@Scheduled(cron = "0 0 6,11 15 JAN ?")
	@Scheduled(cron = "0 0 6,11 20 FEB ?")
    public void cierreAnualImssReproceso() {
        if (!cierreAnualIMSSService.processIsFinished()) {
            logger.info("Se inicia la ejecucion del reproceso");
            ejecucionBatchReproceso();
        } else {
            logger.info("El proceso de cierreAnualIMSS ya ha finalizado");
        }
    }

	private void ejecucionBatchReproceso() {
        if (!cierreAnualIMSSService.processIsFinished()) {
            logger.info("Se inicia la ejecucion del reproceso");
            List<Boolean> jobExecutionStatus = new ArrayList<>();
            try {
                Set<Long> keyList = cierreAnualIMSSService.getFirstJobFailedKeyList();
                for (long dely : keyList) {
                    JobParameters parameters = new JobParametersBuilder()
                            .addLong(CierreAnualImssConstants.KEY_PARAM_DEL, dely)
                            .addDate(CierreAnualImssConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                            .addString(CierreAnualImssConstants.REPROCESS_FLAG, Boolean.TRUE.toString())
                            .toJobParameters();
                    JobExecution jobExecution = jobLauncherSchedule.run(cierreAnualIMSSJob, parameters);
                    logger.info("Se proceso el archivo de la delegacion {} con estatus {}",
                            NumberUtils.getDel(dely),
                            jobExecution.getExitStatus());
                    jobExecutionStatus.add(ExitStatus.COMPLETED.getExitCode().equals(jobExecution.getExitStatus().getExitCode()));
                }
                JobParameters parameters = new JobParametersBuilder()
                        .addDate(CierreAnualImssConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                        .addString(CierreAnualImssConstants.NOT_ERROR_FLAG_PARAM, jobExecutionStatus.stream()
                                .reduce(Boolean.TRUE, Boolean::logicalAnd).toString())
                        .addString(CierreAnualImssConstants.REPROCESS_FLAG, Boolean.TRUE.toString())
                        .toJobParameters();
                JobExecution jobExecutionNext = jobLauncherSchedule.run(cierreAnualIMSSNextJob, parameters);
                logger.info("Se realizo el envio de los archivos a los servidores sftp con status {}", jobExecutionNext.getExitStatus());
            } catch (Exception e) {
                logger.error("Error al ejecutar la tarea", e);
            }
        } else {
            logger.info("El proceso de confronta ya ha finalizado");
        }
		
	}
	
    

}
