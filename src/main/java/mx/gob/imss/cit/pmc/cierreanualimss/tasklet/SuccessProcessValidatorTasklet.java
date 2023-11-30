package mx.gob.imss.cit.pmc.cierreanualimss.tasklet;

import mx.gob.imss.cit.pmc.cierreanualimss.enums.ProcessActionEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.ProcessControlRepository;

import org.springframework.batch.core.ExitStatus;
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
public class SuccessProcessValidatorTasklet implements Tasklet {

    @Autowired
    private ProcessControlRepository processControlRepository;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        boolean getInfoSuccessfully = processControlRepository.validateAction(ProcessActionEnum.GET_INFO.getDesc());
        boolean backupInfoSuccessfully = processControlRepository.validateAction(ProcessActionEnum.BACKUP_INFO.getDesc());
        boolean generateFilesSuccessfully = processControlRepository.validateAction(ProcessActionEnum.GENERATE_FILES.getDesc());
        boolean storeFilesSuccessfully = processControlRepository.validateAction(ProcessActionEnum.FILES_STORAGE.getDesc());
        stepExecution.setExitStatus(getInfoSuccessfully && backupInfoSuccessfully &&
                generateFilesSuccessfully && storeFilesSuccessfully ? ExitStatus.COMPLETED : ExitStatus.FAILED);
        return RepeatStatus.FINISHED;
    }

}
