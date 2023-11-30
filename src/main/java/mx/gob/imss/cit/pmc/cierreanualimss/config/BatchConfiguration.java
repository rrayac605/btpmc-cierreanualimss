package mx.gob.imss.cit.pmc.cierreanualimss.config;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.SneakyThrows;
import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.ChangeDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.MovementDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.RespaldoCierreAnualIMSSDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.RespaldoRiesgosDescartadosImssDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.Sftp1Properties;
import mx.gob.imss.cit.pmc.cierreanualimss.listener.JobCierreAnualIMSSListener;
import mx.gob.imss.cit.pmc.cierreanualimss.listener.MongoProcessorListener;
import mx.gob.imss.cit.pmc.cierreanualimss.listener.MongoReaderListener;
import mx.gob.imss.cit.pmc.cierreanualimss.listener.MongoWriterListener;
import mx.gob.imss.cit.pmc.cierreanualimss.listener.StepCierreAnualIMSSListener;
import mx.gob.imss.cit.pmc.cierreanualimss.processor.BackupCambiosRechazadosProcessor;
import mx.gob.imss.cit.pmc.cierreanualimss.processor.BackupChangeProcessor;
import mx.gob.imss.cit.pmc.cierreanualimss.processor.BackupMovementProcessor;
import mx.gob.imss.cit.pmc.cierreanualimss.processor.BackupMovimientosRechazadosProcessor;
import mx.gob.imss.cit.pmc.cierreanualimss.processor.CierreAnualIMSSChangeProcessor;
import mx.gob.imss.cit.pmc.cierreanualimss.processor.CierreAnualIMSSMovementProcessor;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.AuditFailedTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.AuditTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.EmailOOADTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.EmailTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.FileControlFailedTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.FileValidationTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.FooterTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.GenerateFileSuccessTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.HeaderTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.ProcessControlFailedTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.ProcessControlTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.RollBackTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.SftpUploadFileTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.tasklet.SuccessProcessValidatorTasklet;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.ExitStatusCierreAnualIMSS;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.NumberUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.ReaderUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.StringUtils;

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = { "mx.gob.imss.cit.pmc.cierreanualimss" })
public class BatchConfiguration extends DefaultBatchConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
    
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private HeaderTasklet headerTasklet;

    @Autowired
    private SftpUploadFileTasklet sftpUploadFileTasklet;

    @Autowired
    private ProcessControlTasklet processControlTasklet;

    @Autowired
    private ProcessControlFailedTasklet processControlFailedTasklet;

    @Autowired
    private AuditTasklet auditTasklet;

    @Autowired
    private AuditFailedTasklet auditFailedTasklet;

    @Autowired
    private GenerateFileSuccessTasklet generateFileSuccessTasklet;

    @Autowired
    private FileControlFailedTasklet fileControlFailedTasklet;

    @Autowired
    private FileValidationTasklet fileValidationTasklet;

    @Autowired
    private EmailTasklet emailTasklet;

    @Autowired
    private EmailOOADTasklet emailOOADTasklet;

    @Autowired
    private SuccessProcessValidatorTasklet successProcessValidatorTasklet;

    @Autowired
    private RollBackTasklet rollBackTasklet;

    @Autowired
    private MongoReaderListener<MovementDTO> movementReaderListener;

    @Autowired
    private MongoReaderListener<ChangeDTO> changeReaderListener;
    
    @Autowired
    private MongoReaderListener<MovementDTO> movimientosDescartadosReaderListener;

    @Autowired
    private MongoReaderListener<ChangeDTO> cambiosDescartadosReaderListener;

    @Autowired
    private MongoProcessorListener<MovementDTO, String> movementProcessorListener;

    @Autowired
    private MongoProcessorListener<ChangeDTO, String> changeProcessorListener;

    @Autowired
    private MongoProcessorListener<MovementDTO, RespaldoCierreAnualIMSSDTO> backupMovementProcessorListener;

    @Autowired
    private MongoProcessorListener<ChangeDTO, RespaldoCierreAnualIMSSDTO> backupChangeProcessorListener;
    
    @Autowired
    private MongoProcessorListener<MovementDTO, RespaldoRiesgosDescartadosImssDTO> backupMovimientosRechazadosListener;

    @Autowired
    private MongoProcessorListener<ChangeDTO, RespaldoRiesgosDescartadosImssDTO> backupCambiosRechazadosListener;

    @Autowired
    private MongoWriterListener<String> fileWriterListener;

    @Autowired
    private MongoWriterListener<MovementDTO> movementWriterListener;

    @Autowired
    private MongoWriterListener<ChangeDTO> changeWriterListener;

    @Autowired
    private CierreAnualIMSSMovementProcessor cierreAnualIMSSMovementProcessor;

    @Autowired
    private CierreAnualIMSSChangeProcessor cierreAnualIMSSChangeProcessor;

    @Autowired
    private BackupMovementProcessor backupMovementProcessor;

    @Autowired
    private BackupChangeProcessor backupChangeProcessor;
    
    @Autowired
    private BackupMovimientosRechazadosProcessor backupMovimientosRechazadosProcessor;

    @Autowired
    private BackupCambiosRechazadosProcessor backupCambiosRechazadosProcessor;

    @Autowired
    private StepCierreAnualIMSSListener stepCierreAnualIMSSListener;

    @Autowired
    private JobCierreAnualIMSSListener jobCierreAnualIMSSListener;

    @Bean("batchTransactionManager")
    @NonNull
    public PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @SneakyThrows
    @NonNull
    @Bean("batchJobRepository")
    public JobRepository getJobRepository() {
        return Objects.requireNonNull(new MapJobRepositoryFactoryBean(getTransactionManager()).getObject());
    }

    @Bean(name = "cierreAnualIMSSJob")
    public Job cierreAnualIMSSJob() {
        return jobBuilderFactory.get("cierreAnualIMSSJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobCierreAnualIMSSListener)
                .start(fileCreationFlow())
                .on(ExitStatus.FAILED.getExitCode())
                .to(failToCreateFileFlow())
                .from(fileCreationFlow())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(backupDataFlow())
                .on(ExitStatus.FAILED.getExitCode())
                .to(failToBackupDataFlow())
                .from(backupDataFlow())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(generateFileSuccessFlow())
                .end()
                .build();
    }

    @Bean
    public Flow fileCreationFlow() {
        return new FlowBuilder<Flow>("fileCreationFlow")
                .start(headerStep())
                .next(movementStep())
                .next(changeStep())
                .next(processControlStep())
                .end();

    }

    @Bean
    public Flow failToCreateFileFlow() {
        return new FlowBuilder<Flow>("failToCreateFileFlow")
                .start(processControlFailedStep())
                .next(fileControlFailedStep())
                .next(auditFailedStep())
                .end();
    }

    @Bean
    public Flow backupDataFlow() {
        return new FlowBuilder<Flow>("backupDataFlow")
                .start(backupMovementStep())
                .next(backupChangeStep())
                .next(backupMovimientosDescartadosStep())
                .next(backupMovimientosDescartadosAnterioresStep())
                .next(backupMovimientosDescartadosPosterioresStep())
                .next(backupCambiosDescartadosStep())
                .next(backupCambiosDescartadosAnterioresStep())
                .next(backupCambiosDescartadosPosterioresStep())
                .next(processControlStep())
                .next(auditStep())
                .end();
    }

    @Bean
    public Flow failToBackupDataFlow() {
        return new FlowBuilder<Flow>("failToBackupDataFlow")
                .start(rollBackBackupStep())
                .next(processControlFailedStep())
                .next(auditFailedStep())
                .end();
    }

    @Bean
    public Flow generateFileSuccessFlow() {
        return new FlowBuilder<Flow>("generateFileSuccessFlow")
                .start(generateFileSuccessStep())
                .end();
    }

    @Bean
    public Step headerStep() {
        return stepBuilderFactory.get("headerStep")
                .tasklet(headerTasklet)
                .listener(stepCierreAnualIMSSListener)
                .build();
    }

    /**Datos Normales**/
    @Bean
    public Step movementStep() {
        return stepBuilderFactory.get("movementStep")
                .<MovementDTO, String>chunk(CierreAnualImssConstants.CHUNK_SIZE)
                .reader(movementReader(CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_STRING))
                .listener(movementReaderListener)
                .processor(cierreAnualIMSSMovementProcessor)
                .listener(movementProcessorListener)
                .writer(fileWriter(CierreAnualImssConstants.WILL_BE_INJECTED_SE, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG))
                .listener(fileWriterListener)
                .listener(stepCierreAnualIMSSListener)
                .taskExecutor(taskExecutor())
                .throttleLimit(CierreAnualImssConstants.POOL_SIZE)
                .build();
    }

    @Bean
    @StepScope
    public MongoItemReader<MovementDTO> movementReader(@Value("#{jobParameters[delegacion]}") Long del,
            											@Value("#{jobParameters[subDelegacion]}") Long subDel,
                                                       @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(del);
        logger.info("Step query {}", ReaderUtils.buildMovimientosJSONQuery(del.intValue(), subDelList));
        return new MongoItemReaderBuilder<MovementDTO>()
                .name("movementReader")
                .template(mongoOperations)
                .targetType(MovementDTO.class)
                .sorts(CierreAnualImssConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildMovimientosJSONQuery(del.intValue(), subDelList))
                .pageSize(CierreAnualImssConstants.CHUNK_SIZE)
                .build();
    }

    @Bean
    public Step changeStep() {
        return stepBuilderFactory.get("changeStep")
                .<ChangeDTO, String>chunk(CierreAnualImssConstants.CHUNK_SIZE)
                .reader(changeReader(CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_STRING))
                .listener(changeReaderListener)
                .processor(cierreAnualIMSSChangeProcessor)
                .listener(changeProcessorListener)
                .writer(fileWriter(CierreAnualImssConstants.WILL_BE_INJECTED_SE, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG))
                .listener(fileWriterListener)
                .listener(stepCierreAnualIMSSListener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public MongoItemReader<ChangeDTO> changeReader(@Value("#{jobParameters[delegacion]}") Long del,
            										@Value("#{jobParameters[subDelegacion]}") Long subDel,
                                                   @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(del);
//        Integer del = NumberUtils.getDel(key);
        logger.info("Step query {}", ReaderUtils.buildCambiosJSONQuery(del.intValue(), subDelList));
        return new MongoItemReaderBuilder<ChangeDTO>()
                .name("changeReader")
                .template(mongoOperations)
                .targetType(ChangeDTO.class)
                .sorts(CierreAnualImssConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildCambiosJSONQuery(del.intValue(), subDelList))
                .pageSize(CierreAnualImssConstants.CHUNK_SIZE)
                .build();
    }
    
    @Bean
    @StepScope
    public FlatFileItemWriter<String> fileWriter(@Value("#{stepExecution}")@Nullable StepExecution stepExecution,
                                                 @Value("#{jobParameters[delegacion]}") Long del,
                                                 @Value("#{jobParameters[subDelegacion]}") Long subDel) {
        FlatFileItemWriter<String> writer = new FlatFileItemWriterBuilder<String>()
                .name("fileWriter")
                .resource(new FileSystemResource(StringUtils
                        .buildFileName(del, CierreAnualImssConstants.BASE_PATH_SERVER)))
                .append(Boolean.TRUE)
                .lineAggregator(lineAggregator())
                .build();
        assert stepExecution != null;
        writer.open(stepExecution.getExecutionContext());
        return writer;
    }

    @Bean
    public LineAggregator<String> lineAggregator() {
        DelimitedLineAggregator<String> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        return lineAggregator;
    }

    @Bean
    public Step processControlStep() {
        return stepBuilderFactory.get("processControlStep")
                .tasklet(processControlTasklet)
                .build();
    }

    @Bean
    public Step processControlFailedStep() {
        return stepBuilderFactory.get("processControlFailedStep")
                .tasklet(processControlFailedTasklet)
                .build();
    }

    @Bean
    public Step auditStep() {
        return stepBuilderFactory.get("auditStep")
                .tasklet(auditTasklet)
                .build();
    }

    @Bean
    public Step auditFailedStep() {
        return stepBuilderFactory.get("auditFailedStep")
                .tasklet(auditFailedTasklet)
                .build();
    }

    @Bean
    public Step generateFileSuccessStep() {
        return stepBuilderFactory.get("generateFileSuccessStep")
                .tasklet(generateFileSuccessTasklet)
                .build();
    }

    @Bean
    public Step fileControlFailedStep() {
        return stepBuilderFactory.get("fileControlFailedStep")
                .tasklet(fileControlFailedTasklet)
                .build();
    }
        
    @Bean
    public Step backupMovementStep() {
        return stepBuilderFactory.get("backupMovementStep")
                .<MovementDTO, RespaldoCierreAnualIMSSDTO>chunk(CierreAnualImssConstants.CHUNK_SIZE)
                .reader(movementReader(CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_STRING))
                .listener(movementReaderListener)
                .processor(backupMovementProcessor)
                .listener(backupMovementProcessorListener)
                .writer(backupWriter())
                .listener(movementWriterListener)
                .listener(stepCierreAnualIMSSListener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step backupChangeStep() {
        return stepBuilderFactory.get("backupChangeStep")
                .<ChangeDTO, RespaldoCierreAnualIMSSDTO>chunk(CierreAnualImssConstants.CHUNK_SIZE)
                .reader(changeReader(CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_STRING))
                .listener(changeReaderListener)
                .processor(backupChangeProcessor)
                .listener(backupChangeProcessorListener)
                .writer(backupWriter())
                .listener(changeWriterListener)
                .listener(stepCierreAnualIMSSListener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean MongoItemWriter<RespaldoCierreAnualIMSSDTO> backupWriter() {
        return new MongoItemWriterBuilder<RespaldoCierreAnualIMSSDTO>()
                .template(mongoOperations)
                .collection("MCB_RESPALDO_CIERRE_ANUAL_CASUISTICA_IMSS")
                .build();
    }

    @Bean MongoItemWriter<RespaldoRiesgosDescartadosImssDTO> backupRiesgosWriter() {
        return new MongoItemWriterBuilder<RespaldoRiesgosDescartadosImssDTO>()
                .template(mongoOperations)
                .collection("MCB_RESPALDO_RIESGOS_DESCARTADOS_IMSS")
                .build();
    }
    
    @Bean
    public Step rollBackBackupStep() {
        return stepBuilderFactory.get("rollBackBackupStep")
                .tasklet(rollBackTasklet)
                .build();
    }

    @Bean
    public Step sftpUploadFileStep() {
        return stepBuilderFactory.get("sftpUploadFileStep")
                .tasklet(sftpUploadFileTasklet)
                .listener(stepCierreAnualIMSSListener)
                .build();
    }

    @Bean("cierreAnualIMSSNextJob")
    public Job cierreAnualIMSSNextJob() {
        return jobBuilderFactory.get("cierreAnualIMSSNextJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobCierreAnualIMSSListener)
                .start(emailOOADStep())
                .next(fileValidationStep())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(validationSuccessFlow())
                .from(fileValidationStep())
                .on(ExitStatusCierreAnualIMSS.GENERATION_FILE_FAILED.getExitCode())
                .to(validationFailedFlow())
                .end()
                .build();
    }

    @Bean
    public Step emailOOADStep() {
        return stepBuilderFactory.get("emailOOADStep")
                .tasklet(emailOOADTasklet)
                .build();
    }

    @Bean
    public Step fileValidationStep() {
        return stepBuilderFactory.get("fileValidationStep")
                .tasklet(fileValidationTasklet)
                .listener(stepCierreAnualIMSSListener)
                .build();
    }

    @Bean
    public Flow validationSuccessFlow() {
        return new FlowBuilder<Flow>("validationSuccessFlow")
                .start(processControlStep())
                .next(uploadFilesFlow())
                .end();
    }

    @Bean
    public Flow validationFailedFlow() {
        return new FlowBuilder<Flow>("validationFailedFlow")
                .start(processControlFailedStep())
                .next(auditFailedStep())
                .next(uploadFilesFlow())
                .end();
    }

    @Bean
    public Flow uploadFilesFlow() {
        return new FlowBuilder<Flow>("validationSuccessFlow")
                .start(sftpUploadFileStep())
                .next(fileValidationStep())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(uploadSuccessFlow())
                .from(fileValidationStep())
                .on(ExitStatusCierreAnualIMSS.UPLOAD_SFTP1_FAILED.getExitCode())
                .to(uploadFailedFlow())
                .end();
    }

    @Bean
    public Flow uploadSuccessFlow() {
        return new FlowBuilder<Flow>("uploadSuccessFlow")
                .start(processControlStep())
                .next(auditStep())
                .next(emailStep())
                .end();
    }

    @Bean
    public Flow uploadFailedFlow() {
        return new FlowBuilder<Flow>("uploadFailedFlow")
                .start(processControlFailedStep())
                .next(auditFailedStep())
                .end();
    }


    @Bean
    public Flow bankSuccessFlow() {
        return new FlowBuilder<Flow>("bankSuccessFlow")
                .start(processControlStep())
                .next(auditStep())
                .next(emailStep())
                .next(successProcessValidationStep())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(processSuccessFlow())
                .build();
    }

    @Bean
    public Flow bankFailedFlow() {
        return new FlowBuilder<Flow>("bankFailedFlow")
                .start(processControlFailedStep())
                .next(auditFailedStep())
                .end();
    }

    @Bean
    public Step emailStep() {
        return stepBuilderFactory.get("emailStep")
                .tasklet(emailTasklet)
                .build();
    }

    @Bean
    public Step successProcessValidationStep() {
        return stepBuilderFactory.get("successProcessValidationStep")
                .tasklet(successProcessValidatorTasklet)
                .listener(stepCierreAnualIMSSListener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Flow processSuccessFlow() {
        return new FlowBuilder<Flow>("processSuccessFlow")
                .start(processControlStep())
                .build();
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(CierreAnualImssConstants.POOL_SIZE);
        taskExecutor.setMaxPoolSize(CierreAnualImssConstants.POOL_SIZE);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }


    /********************************************************************************************************************************************************************************/
    /**Datos descartados*****************************************************************************************************/
    /********************************************************************************************************************************************************************************/

    @Bean
    public Step backupMovimientosDescartadosStep() {
        return stepBuilderFactory.get("backupMovimientosDescartadostStep")
                .<MovementDTO, RespaldoRiesgosDescartadosImssDTO>chunk(CierreAnualImssConstants.CHUNK_SIZE)
                .reader(movimientosDescartadosReader(CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_STRING))
                .listener(movimientosDescartadosReaderListener)
                .processor(backupMovimientosRechazadosProcessor)
                .listener(backupMovimientosRechazadosListener)
                .writer(backupRiesgosWriter())
                .listener(movementWriterListener)
                .listener(stepCierreAnualIMSSListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    public Step backupMovimientosDescartadosAnterioresStep() {
        return stepBuilderFactory.get("backupMovimientosDescartadostStep")
                .<MovementDTO, RespaldoRiesgosDescartadosImssDTO>chunk(CierreAnualImssConstants.CHUNK_SIZE)
                .reader(movimientosDescartadosAnterioresReader(CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_STRING))
                .listener(movimientosDescartadosReaderListener)
                .processor(backupMovimientosRechazadosProcessor)
                .listener(backupMovimientosRechazadosListener)
                .writer(backupRiesgosWriter())
                .listener(movementWriterListener)
                .listener(stepCierreAnualIMSSListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    public Step backupMovimientosDescartadosPosterioresStep() {
        return stepBuilderFactory.get("backupMovimientosDescartadostStep")
                .<MovementDTO, RespaldoRiesgosDescartadosImssDTO>chunk(CierreAnualImssConstants.CHUNK_SIZE)
                .reader(movimientosDescartadosPosterioresReader(CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_STRING))
                .listener(movimientosDescartadosReaderListener)
                .processor(backupMovimientosRechazadosProcessor)
                .listener(backupMovimientosRechazadosListener)
                .writer(backupRiesgosWriter())
                .listener(movementWriterListener)
                .listener(stepCierreAnualIMSSListener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step backupCambiosDescartadosStep() {
        return stepBuilderFactory.get("backupCambiosDescartadosStep")
                .<ChangeDTO, RespaldoRiesgosDescartadosImssDTO>chunk(CierreAnualImssConstants.CHUNK_SIZE)
                .reader(cambiosDescartadosReader(CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_STRING))
                .listener(cambiosDescartadosReaderListener)
                .processor(backupCambiosRechazadosProcessor)
                .listener(backupCambiosRechazadosListener)
                .writer(backupRiesgosWriter())
                .listener(changeWriterListener)
                .listener(stepCierreAnualIMSSListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    public Step backupCambiosDescartadosAnterioresStep() {
        return stepBuilderFactory.get("backupCambiosDescartadosStep")
                .<ChangeDTO, RespaldoRiesgosDescartadosImssDTO>chunk(CierreAnualImssConstants.CHUNK_SIZE)
                .reader(cambiosDescartadosAnterioresReader(CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_STRING))
                .listener(cambiosDescartadosReaderListener)
                .processor(backupCambiosRechazadosProcessor)
                .listener(backupCambiosRechazadosListener)
                .writer(backupRiesgosWriter())
                .listener(changeWriterListener)
                .listener(stepCierreAnualIMSSListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    @Bean
    public Step backupCambiosDescartadosPosterioresStep() {
        return stepBuilderFactory.get("backupCambiosDescartadosStep")
                .<ChangeDTO, RespaldoRiesgosDescartadosImssDTO>chunk(CierreAnualImssConstants.CHUNK_SIZE)
                .reader(cambiosDescartadosPosterioresReader(CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_LONG, CierreAnualImssConstants.WILL_BE_INJECTED_STRING))
                .listener(cambiosDescartadosReaderListener)
                .processor(backupCambiosRechazadosProcessor)
                .listener(backupCambiosRechazadosListener)
                .writer(backupRiesgosWriter())
                .listener(changeWriterListener)
                .listener(stepCierreAnualIMSSListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    @StepScope
    public MongoItemReader<MovementDTO> movimientosDescartadosReader(@Value("#{jobParameters[delegacion]}") Long del,
            											@Value("#{jobParameters[subDelegacion]}") Long subDel,
                                                       @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(del);
        logger.info("Step query {}", ReaderUtils.buildMovimientosDescartadosJSONQuery(del.intValue(), subDelList));
        return new MongoItemReaderBuilder<MovementDTO>()
                .name("movimientosDescartadosReader")
                .template(mongoOperations)
                .targetType(MovementDTO.class)
                .sorts(CierreAnualImssConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildMovimientosDescartadosJSONQuery(del.intValue(), subDelList))
                .pageSize(CierreAnualImssConstants.CHUNK_SIZE)
                .build();
    }
    
    @Bean
    @StepScope
    public MongoItemReader<MovementDTO> movimientosDescartadosAnterioresReader(@Value("#{jobParameters[delegacion]}") Long del,
            											@Value("#{jobParameters[subDelegacion]}") Long subDel,
                                                       @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(del);
        logger.info("Step query Anterior {}", ReaderUtils.buildMovimientosDescartadosAnterioresJSONQuery(del.intValue(), subDelList));
        return new MongoItemReaderBuilder<MovementDTO>()
                .name("movimientosDescartadosReader")
                .template(mongoOperations)
                .targetType(MovementDTO.class)
                .sorts(CierreAnualImssConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildMovimientosDescartadosAnterioresJSONQuery(del.intValue(), subDelList))
                .pageSize(CierreAnualImssConstants.CHUNK_SIZE)
                .build();
    }
    
    @Bean
    @StepScope
    public MongoItemReader<MovementDTO> movimientosDescartadosPosterioresReader(@Value("#{jobParameters[delegacion]}") Long del,
            											@Value("#{jobParameters[subDelegacion]}") Long subDel,
                                                       @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(del);
        logger.info("Step query Posterior {}", ReaderUtils.buildMovimientosDescartadosPosterioresJSONQuery(del.intValue(), subDelList));
        return new MongoItemReaderBuilder<MovementDTO>()
                .name("movimientosDescartadosReader")
                .template(mongoOperations)
                .targetType(MovementDTO.class)
                .sorts(CierreAnualImssConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildMovimientosDescartadosPosterioresJSONQuery(del.intValue(), subDelList))
                .pageSize(CierreAnualImssConstants.CHUNK_SIZE)
                .build();
    }


    @Bean
    @StepScope
    public MongoItemReader<ChangeDTO> cambiosDescartadosReader(@Value("#{jobParameters[delegacion]}") Long del,
            										@Value("#{jobParameters[subDelegacion]}") Long subDel,
                                                   @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(del);
        logger.debug("Step query {}", ReaderUtils.buildCambiosDescartadosJSONQuery(del.intValue(), subDelList));
        return new MongoItemReaderBuilder<ChangeDTO>()
                .name("cambiosDescartadosReader")
                .template(mongoOperations)
                .targetType(ChangeDTO.class)
                .sorts(CierreAnualImssConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildCambiosDescartadosJSONQuery(del.intValue(), subDelList))
                .pageSize(CierreAnualImssConstants.CHUNK_SIZE)
                .build();
    }
    
    @Bean
    @StepScope
    public MongoItemReader<ChangeDTO> cambiosDescartadosAnterioresReader(@Value("#{jobParameters[delegacion]}") Long del,
            										@Value("#{jobParameters[subDelegacion]}") Long subDel,
                                                   @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(del);
        logger.debug("Step query Anterior{}", ReaderUtils.buildCambiosDescartadosAnterioresJSONQuery(del.intValue(), subDelList));
        return new MongoItemReaderBuilder<ChangeDTO>()
                .name("cambiosDescartadosReader")
                .template(mongoOperations)
                .targetType(ChangeDTO.class)
                .sorts(CierreAnualImssConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildCambiosDescartadosAnterioresJSONQuery(del.intValue(), subDelList))
                .pageSize(CierreAnualImssConstants.CHUNK_SIZE)
                .build();
    }
    
    @Bean
    @StepScope
    public MongoItemReader<ChangeDTO> cambiosDescartadosPosterioresReader(@Value("#{jobParameters[delegacion]}") Long del,
            										@Value("#{jobParameters[subDelegacion]}") Long subDel,
                                                   @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(del);
        logger.debug("Step query Posterior{}", ReaderUtils.buildCambiosDescartadosPosterioresJSONQuery(del.intValue(), subDelList));
        return new MongoItemReaderBuilder<ChangeDTO>()
                .name("cambiosDescartadosReader")
                .template(mongoOperations)
                .targetType(ChangeDTO.class)
                .sorts(CierreAnualImssConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildCambiosDescartadosPosterioresJSONQuery(del.intValue(), subDelList))
                .pageSize(CierreAnualImssConstants.CHUNK_SIZE)
                .build();
    }
    
    

    
    /********************************************************************************************************************************************************************************/
    /********************************************************************************************************************************************************************************/
  
}
