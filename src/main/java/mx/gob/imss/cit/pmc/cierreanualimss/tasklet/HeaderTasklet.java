package mx.gob.imss.cit.pmc.cierreanualimss.tasklet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.bson.Document;
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
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.ChangeDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.CountDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.MovementDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.Sftp1Properties;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.CountRepository;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.NumberUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.ReaderUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.StringUtils;

@Component
@StepScope
public class HeaderTasklet implements Tasklet {

	private static final Logger logger = LoggerFactory.getLogger(HeaderTasklet.class);

	@Autowired
	private Sftp1Properties sftp1Properties;

	@Value("#{stepExecution}")
	private StepExecution stepExecution;

	@SneakyThrows
	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
		logger.debug("HEADER");
		Long del = stepExecution.getJobParameters().getLong(CierreAnualImssConstants.KEY_PARAM_DEL);
		Integer delFinal = NumberUtils.getDel(del);
		List<Integer> delegaciones = NumberUtils.getSubDelList(del);
		String subDel = StringUtils.subDelToString(delegaciones);
		// Agregar exception para EA003

		String filePath = StringUtils.buildFilePath(CierreAnualImssConstants.BASE_PATH_SERVER);
		File directory = new File(filePath);
		String nombreArchivo = StringUtils.nombreArchivo(del);
		try {
		if (directory.exists() || directory.mkdirs()) {
			assert stepExecution != null;
			String header = CierreAnualImssConstants.CTROL_HEADER
					.concat(StringUtils.getDelegacionFiller(delFinal, subDel))
					.concat(CierreAnualImssConstants.CTROL_CVE_CTL).concat(CierreAnualImssConstants.CTROL_CASUI)
					.concat(CierreAnualImssConstants.CTROL_CVE_CTL).concat(DateUtils.getAACCC())
					.concat(StringUtils.safeAddZero(0, 73)).concat("\n");
			logger.debug(filePath.concat(nombreArchivo));
			BufferedWriter writter = new BufferedWriter(new FileWriter(filePath.concat(nombreArchivo), Boolean.TRUE));
			writter.write(header);
			writter.close();
		}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return RepeatStatus.FINISHED;
	}

}
