package mx.gob.imss.cit.pmc.cierreanualimss.controller;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.services.CierreAnualIMSSService;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.NumberUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@Api(value = "Ejecución batch PMC", tags = { "Ejecución batch PMC Rest" })
@RequestMapping("/msBatchCierreAnualIMSS")
public class BatchCierreAnualIMSSControllerImpl {

    Logger logger = LoggerFactory.getLogger("BatchCierreAnualImssControllerImpl");

    @Autowired
    @Qualifier("jobLauncherController")
    private SimpleJobLauncher jobLauncherController;

    @Autowired
    @Qualifier("cierreAnualIMSSJob")
    private Job cierreAnualIMSSJob;

    @Autowired
    @Qualifier("cierreAnualIMSSNextJob")
    private Job cierreAnualIMSSNextJob;

    @Autowired
    private CierreAnualIMSSService cierreAnualIMSSService;

    @Bean
    public SimpleJobLauncher jobLauncherController(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @PostMapping(value = "/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> execute() {
        logger.info("Se inicia la ejecucion del proceso");
        ResponseEntity<?> response = null;
        List<Boolean> jobExecutionStatus = new ArrayList<>();
        try {
            for (int key : CierreAnualImssConstants.DEL_SUBDEL.keySet()) {
                JobParameters parameters = new JobParametersBuilder()
                        .addLong(CierreAnualImssConstants.KEY_PARAM_DEL, (long) key)
                        .addDate(CierreAnualImssConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                        .addString(CierreAnualImssConstants.REPROCESS_FLAG, Boolean.FALSE.toString())
                        .toJobParameters();
                JobExecution jobExecution = jobLauncherController.run(cierreAnualIMSSJob, parameters);
                logger.info("Se proceso el archivo de la delegacion {} con estatus {}",
                        NumberUtils.getDel((long) key),
                        jobExecution.getExitStatus());
                jobExecutionStatus.add(ExitStatus.COMPLETED.getExitCode().equals(jobExecution.getExitStatus().getExitCode()));
            }
            JobParameters parameters = new JobParametersBuilder()
                    .addDate(CierreAnualImssConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                    .addString(CierreAnualImssConstants.NOT_ERROR_FLAG_PARAM, jobExecutionStatus.stream()
                            .reduce(Boolean.TRUE, Boolean::logicalAnd).toString())
                    .addString(CierreAnualImssConstants.REPROCESS_FLAG, Boolean.FALSE.toString())
                    .toJobParameters();
            JobExecution jobExecutionNext = jobLauncherController.run(cierreAnualIMSSNextJob, parameters);
            logger.info("Se realizo el envio de los archivos a los servidores sftp con status {}", jobExecutionNext.getExitStatus());
            response = new ResponseEntity<>("Proceso de cierreAnualIMSS ejecutado correctamente", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al ejecutar la tarea", e);
            response = new ResponseEntity<>("Error al ejecutar la tarea", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping(value = "/reprocess", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reprocess() {
        ResponseEntity<?> response;
        if (!cierreAnualIMSSService.processIsFinished()) {
            logger.info("Se inicia la ejecucion del reproceso");
            List<Boolean> jobExecutionStatus = new ArrayList<>();
            try {
                Set<Long> keyList = cierreAnualIMSSService.getFirstJobFailedKeyList();
                for (long key : keyList) {
                    JobParameters parameters = new JobParametersBuilder()
                            .addLong(CierreAnualImssConstants.KEY_PARAM_DEL, key)
                            .addDate(CierreAnualImssConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                            .addString(CierreAnualImssConstants.REPROCESS_FLAG, Boolean.TRUE.toString())
                            .toJobParameters();
                    JobExecution jobExecution = jobLauncherController.run(cierreAnualIMSSJob, parameters);
                    logger.info("Se proceso el archivo de la delegacion {} con estatus {}",
                            NumberUtils.getDel(key),
                            jobExecution.getExitStatus());
                    jobExecutionStatus.add(ExitStatus.COMPLETED.getExitCode().equals(jobExecution.getExitStatus().getExitCode()));
                }
                JobParameters parameters = new JobParametersBuilder()
                        .addDate(CierreAnualImssConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                        .addString(CierreAnualImssConstants.NOT_ERROR_FLAG_PARAM, jobExecutionStatus.stream()
                                .reduce(Boolean.TRUE, Boolean::logicalAnd).toString())
                        .addString(CierreAnualImssConstants.REPROCESS_FLAG, Boolean.TRUE.toString())
                        .toJobParameters();
                JobExecution jobExecutionNext = jobLauncherController.run(cierreAnualIMSSNextJob, parameters);
                logger.info("Se realizo el envio de los archivos a los servidores sftp con status {}", jobExecutionNext.getExitStatus());
                response = new ResponseEntity<>("Proceso de cierreAnualIMSS ejecutado correctamente", HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                response = new ResponseEntity<>("Error al ejecutar la tarea", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.info("El proceso de cierreAnualIMSS ya ha finalizado");
            response = new ResponseEntity<>("El proceso de cierreAnualIMSS ya ha finalizado", HttpStatus.OK);
        }
        return response;
    }

}
