package mx.gob.imss.cit.pmc.cierreanualimss.tasklet;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.ProcessActionEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.FileControlRepository;
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

@Component
@StepScope
public class GenerateFileSuccessTasklet implements Tasklet {

    @Autowired
    private FileControlRepository fileControlRepository;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;
    
	private static final Logger logger = LoggerFactory.getLogger(GenerateFileSuccessTasklet.class);
	
	@Autowired
	private EmailService emailService;


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
    	logger.debug("::::::::::::::::::::::::::::::::::: GenerateFileSuccessTasklet ::::::::::::::::::::::::::::::::::: " );
        Long del = stepExecution.getJobParameters().getLong(CierreAnualImssConstants.KEY_PARAM_DEL);
        fileControlRepository.createCorrect(ProcessActionEnum.FILE_GENERATION.getDesc(), del);
        if(del.equals(42L)) {
        	emailService.sendEmailPasoUno(CierreAnualImssConstants.PASO_UNO);
        	emailService.sendEmailPasoDosTresCuatro(CierreAnualImssConstants.PASO_DOS);
        	emailService.sendEmailPasoDosTresCuatro(CierreAnualImssConstants.PASO_TRES);
        }
        return RepeatStatus.FINISHED;
    }

}
