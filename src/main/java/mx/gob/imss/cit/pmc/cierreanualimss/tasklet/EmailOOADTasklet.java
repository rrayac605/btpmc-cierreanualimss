package mx.gob.imss.cit.pmc.cierreanualimss.tasklet;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.services.CierreAnualIMSSService;
import mx.gob.imss.cit.pmc.cierreanualimss.services.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@StepScope
public class EmailOOADTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(EmailTasklet.class);

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CierreAnualIMSSService cierreAnualIMSSService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    	logger.debug("::::::::::::::::::::::::::::::::::: EmailOOADTasklet ::::::::::::::::::::::::::::::::::: " );
        try {
            boolean isReprocess = Boolean.TRUE.toString().equals(
                    stepExecution.getJobParameters().getString(CierreAnualImssConstants.REPROCESS_FLAG));
            if (Boolean.TRUE.toString().equals(stepExecution.getJobParameters().getString(CierreAnualImssConstants.NOT_ERROR_FLAG_PARAM))) {
                return RepeatStatus.FINISHED;
            }
            Map<String, Set<Integer>> templateAndKey = cierreAnualIMSSService.getFirstJobFailedParams(isReprocess);
            Map<String, Set<Integer>> llaves = cierreAnualIMSSService.obtenerCorrectos(isReprocess);
            for (String templateName : templateAndKey.keySet()) {
            	if(llaves.keySet().size() == 0) {
            		emailService.sendEmailGetBackupInfoFailedList(templateName, templateAndKey.get(templateName).stream()
                            .map(Long::new).collect(Collectors.toSet()));
                    logger.info("Correo a enviar EmailOOADTasklet -> {}", templateName);
            	} else {
            		for(String nombreTemplate : llaves.keySet()) {
                		if (templateAndKey.get(templateName).size() > 0 && llaves.get(nombreTemplate).size() >= 0) {
                            emailService.sendEmailToOOADList(templateName, templateAndKey.get(templateName).stream()
                                    .map(Long::new).collect(Collectors.toSet()),
                                    llaves.get(nombreTemplate).stream().map(Long::new).collect(Collectors.toSet()));
                            logger.info("Correo a enviar EmailOOADTasklet -> {}", templateName);
                        }
                	}
            	}
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return RepeatStatus.FINISHED;
    }


}
