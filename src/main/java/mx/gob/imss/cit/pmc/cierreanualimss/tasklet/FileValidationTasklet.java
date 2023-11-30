package mx.gob.imss.cit.pmc.cierreanualimss.tasklet;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.ProcessActionEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.FileControlRepository;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.ExitStatusCierreAnualIMSS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class FileValidationTasklet implements Tasklet {

	private static final Logger logger = LoggerFactory.getLogger(FileValidationTasklet.class);

	@Value("#{stepExecution}")
	private StepExecution stepExecution;

	@Autowired
	private FileControlRepository fileControlRepository;

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		String fromStep = stepExecution.getJobExecution().getExecutionContext()
				.getString(CierreAnualImssConstants.PAST_STEP_PARAM);
//        boolean isSftp1 = CierreAnualImssConstants.SFTP_UPLOAD_FILE_STEP.equals(fromStep);
//		String action = ProcessActionEnum.FILE_STORAGE.getDesc();
		boolean isValid = fileControlRepository.validate();
		if (!isValid) {
			stepExecution.getJobExecution().getExecutionContext().putString(CierreAnualImssConstants.IS_TERMINATED_ONLY,
					Boolean.TRUE.toString());
		}
		ExitStatusCierreAnualIMSS exitStatusCierreAnualIMSS = ExitStatusCierreAnualIMSS.GENERATION_FILE_FAILED;
		stepExecution.setExitStatus(isValid ? ExitStatus.COMPLETED : exitStatusCierreAnualIMSS);
		return RepeatStatus.FINISHED;
	}
}
