package mx.gob.imss.cit.pmc.cierreanualimss.listener;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.NumberUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@JobScope
public class JobCierreAnualIMSSListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(JobCierreAnualIMSSListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Job name: {}", jobExecution.getJobInstance().getJobName());
        jobExecution.getExecutionContext().putString(CierreAnualImssConstants.PAST_STEP_PARAM, CierreAnualImssConstants.EMPTY);
        jobExecution.getExecutionContext().putString(CierreAnualImssConstants.IS_TERMINATED_ONLY, Boolean.FALSE.toString());
        if (CierreAnualImssConstants.CIERREANUALIMSS_JOB.equals(jobExecution.getJobInstance().getJobName())) {
            Long key = jobExecution.getJobParameters().getLong(CierreAnualImssConstants.KEY_PARAM_DEL);
            Integer del = NumberUtils.getDel(key);
            List<Integer> subDelList = NumberUtils.getSubDelList(key);
            logger.info("-------------------------{}-------------------------", DateUtils.getCurrentMexicoDate());
            logger.info("Comienza la ejecucion de el job con del {} para la delegacion {} y sub delegaciones {}", key, del, subDelList);
            logger.info("--------------------------------------------------------------------------");
        }
        
        if (CierreAnualImssConstants.CIERREANUALIMSS_NEXT_JOB.equals(jobExecution.getJobInstance().getJobName())) {
            logger.info("-------------------------{}-------------------------", DateUtils.getCurrentMexicoDate());
            logger.info("Comienza la ejecucion de el NEXT JOB ");
            logger.info("--------------------------------------------------------------------------");
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (CierreAnualImssConstants.CIERREANUALIMSS_JOB.equals(jobExecution.getJobInstance().getJobName())) {
            Long key = jobExecution.getJobParameters().getLong(CierreAnualImssConstants.KEY_PARAM_DEL);
            Integer del = NumberUtils.getDel(key);
            List<Integer> subDelList = NumberUtils.getSubDelList(key);
            logger.info("-------------------------{}-------------------------", DateUtils.getCurrentMexicoDate());
            logger.info("Finaliza la ejecucion de el job con del {} para la delegacion {} y sub delegaciones {}", key, del, subDelList);
            logger.info("--------------------------------------------------------------------------");
        }
        if (CierreAnualImssConstants.CIERREANUALIMSS_NEXT_JOB.equals(jobExecution.getJobInstance().getJobName())) {
            logger.info("-------------------------{}-------------------------", DateUtils.getCurrentMexicoDate());
            logger.info("Finaliza la ejecucion de el NEXT JOB ");
            logger.info("--------------------------------------------------------------------------");
        }
    }
}
