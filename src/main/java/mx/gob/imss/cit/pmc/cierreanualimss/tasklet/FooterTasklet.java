package mx.gob.imss.cit.pmc.cierreanualimss.tasklet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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
import mx.gob.imss.cit.pmc.cierreanualimss.repository.impl.CountRepositoryImpl;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.NumberUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.ReaderUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.StringUtils;

@Component
@StepScope
public class FooterTasklet implements Tasklet {
	
	@Autowired
	private Sftp1Properties sftp1Properties;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Autowired
    private CountRepository countRepository;

    private static final Logger logger = LoggerFactory.getLogger(FooterTasklet.class);

    @SneakyThrows
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        try {
            Long del = stepExecution.getJobParameters().getLong(CierreAnualImssConstants.KEY_PARAM_DEL);
            List<Integer> subDelList = NumberUtils.getSubDelList(del);
//            Integer del = NumberUtils.getDel(del);
//            TypedAggregation<CountDTO> test = ReaderUtils.buildMovementsCountAggregation(del.intValue(), subDelList);

            boolean isReprocess = Boolean.TRUE.toString().equals(
                    stepExecution.getJobParameters().getString(CierreAnualImssConstants.REPROCESS_FLAG));
            TypedAggregation<MovementDTO> movementsAggregation = ReaderUtils.buildMovementsCountAggregation(del.intValue(), 
            		subDelList, CierreAnualImssConstants.CRITERIA_CONSECUENCIA, CierreAnualImssConstants.CRITERIA_CONSECUENCIA,1);
            TypedAggregation<ChangeDTO> changesAggregation = ReaderUtils.buildChangesCountAggregation(del.intValue(), 
            		subDelList, CierreAnualImssConstants.CRITERIA_CONSECUENCIA, CierreAnualImssConstants.CRITERIA_CONSECUENCIA,1);
            logger.info("Aggregacion de movimientos {}", movementsAggregation);
            logger.info("Aggregacion de cambios {}", changesAggregation);
            long count = countRepository.count(movementsAggregation) + countRepository.count(changesAggregation);
            logger.info("Registros insertados {}", count);
            String header = CierreAnualImssConstants.CTROL_HEADER
                    .concat(StringUtils.safeAddZero(del.intValue(), 2))
                    .concat(CierreAnualImssConstants.CTROL_NOMB_ARCH_CTL)
                    .concat(CierreAnualImssConstants.CTROL_CVE_CTL)
                    .concat(DateUtils.getAACCC())
                    .concat(DateUtils.getFileFormattedDate(new Date()))
                    .concat(DateUtils.getFileFormattedHour(new Date()))
                    .concat(StringUtils.safeAddZero((int) count, 10))
                    .concat(CierreAnualImssConstants.FILLER);
            BufferedWriter writter = new BufferedWriter(new FileWriter(StringUtils
                    .buildFileName(del, sftp1Properties.getPath()), Boolean.TRUE));
            writter.write(header);
            writter.close();
            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            stepExecution.getJobExecution().getExecutionContext()
                    .putString(CierreAnualImssConstants.IS_TERMINATED_ONLY, Boolean.TRUE.toString());
            e.printStackTrace();
            return RepeatStatus.CONTINUABLE;
        }
    }
	
	
}
