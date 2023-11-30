package mx.gob.imss.cit.pmc.cierreanualimss.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;

@Component
@StepScope
public class StepCierreAnualIMSSListener implements StepExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(StepCierreAnualIMSSListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        stepExecution.getJobExecution().getExecutionContext().putString(CierreAnualImssConstants.ACTION_PARAM,
                CierreAnualImssConstants.FROM_STEP_ACTION_NAME.get(stepName));
        stepExecution.getJobExecution().getExecutionContext().putString(CierreAnualImssConstants.ACTION_AUDIT_PARAM,
                CierreAnualImssConstants.FROM_STEP_ACTION_AUDIT_NAME.get(stepName));
        stepExecution.getJobExecution().getExecutionContext().putString(CierreAnualImssConstants.PAST_STEP_PARAM,
                stepName);
        return ExitStatus.COMPLETED;
    }

}
