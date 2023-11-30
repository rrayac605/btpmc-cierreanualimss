package mx.gob.imss.cit.pmc.cierreanualimss.tasklet;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.ProcessAuditRepository;

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
public class AuditFailedTasklet implements Tasklet {

    @Autowired
    private ProcessAuditRepository processAuditRepository;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
    	System.out.println( " AuditFailedTasklet : : : : : : : : :" + chunkContext + " : : : : : : : : :" + stepContribution.toString());

        String isTerminatedOnly = stepExecution.getJobExecution().getExecutionContext().getString(CierreAnualImssConstants.IS_TERMINATED_ONLY);
        String fromStepName = stepExecution.getJobExecution().getExecutionContext().getString(CierreAnualImssConstants.PAST_STEP_PARAM);
        String action = CierreAnualImssConstants.FROM_STEP_FAILED_ACTION_AUDIT_NAME.get(fromStepName);
        Long del = stepExecution.getJobParameters().getLong(CierreAnualImssConstants.KEY_PARAM_DEL);
        processAuditRepository.createIncorrect(action, del);
        if (Boolean.TRUE.toString().equals(isTerminatedOnly)) {
            stepExecution.setTerminateOnly();
        }
        return RepeatStatus.FINISHED;
    }

}
