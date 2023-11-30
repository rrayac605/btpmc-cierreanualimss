package mx.gob.imss.cit.pmc.cierreanualimss.tasklet;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.ProcessControlRepository;

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
public class ProcessControlTasklet implements Tasklet {

    @Autowired
    private ProcessControlRepository processControlRepository;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
    	System.out.println( chunkContext + " : : : : : : : : :" + stepContribution.toString());
        String action = stepExecution.getJobExecution().getExecutionContext().getString(CierreAnualImssConstants.ACTION_PARAM);
        Long del = stepExecution.getJobParameters().getLong(CierreAnualImssConstants.KEY_PARAM_DEL);
        System.out.println("action: " + action + " : : : " + stepContribution.toString());
        processControlRepository.createCorrect(action, del);
        return RepeatStatus.FINISHED;
    }
}
