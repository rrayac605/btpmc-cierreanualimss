package mx.gob.imss.cit.pmc.cierreanualimss.tasklet;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.jcraft.jsch.JSchException;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.Sftp1Properties;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.AuditActionEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.EmailTemplateEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.ProcessActionEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.FileControlRepository;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.ProcessAuditRepository;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.ProcessControlRepository;
import mx.gob.imss.cit.pmc.cierreanualimss.services.EmailService;
import mx.gob.imss.cit.pmc.cierreanualimss.sftp.SftpCierreAnualIMSS;
import mx.gob.imss.cit.pmc.cierreanualimss.sftp.SftpClient;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.StringUtils;

@Component
@StepScope
public class SftpUploadFileTasklet implements Tasklet {

	private static final Logger logger = LoggerFactory.getLogger(SftpUploadFileTasklet.class);

	@Autowired
	private Sftp1Properties sftp1Properties;

	@Autowired
	private FileControlRepository fileControlRepository;

	@Autowired
	private ProcessAuditRepository processAuditRepository;
	
	@Autowired
	private ProcessControlRepository processControlRepository;

	@Autowired
	private EmailService emailService;

	@Value("#{stepExecution}")
	private StepExecution stepExecution;

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		String action = ProcessActionEnum.FILE_STORAGE.getDesc();
		String auditAction = AuditActionEnum.ALMACENAR_ARCHIVOS.getDesc();
		String templateName = EmailTemplateEnum.UPLOAD_FILES_FAILED.getDesc();
		String basePath = sftp1Properties.getPath();
		String pasoCuatro = CierreAnualImssConstants.PASO_CUATRO;
		SftpClient sftpClient;
		Set<Long> keyList = fileControlRepository.findKeyListOfGeneratedFiles();
		Set<Long> llaves = fileControlRepository.findKeyListOfGeneratedFiles();
		Set<Long> llavesCorrectas = new HashSet<>();
		Set<Long> exceptionKeyList = new HashSet<>();
		try {
			sftpClient = SftpClient.getSftpClient(sftp1Properties.getHost(), sftp1Properties.getPort(),
					sftp1Properties.getUser(), sftp1Properties.getPass());
		} catch (JSchException e) {
			for (Long key : keyList) {
				fileControlRepository.createError(action, key);
			}
			processAuditRepository.createIncorrect(auditAction, null);
			logger.error("Error de coneccion con el sftp", e);
			emailService.sendEmailToOOADList(templateName, keyList, llaves);
			return RepeatStatus.FINISHED;
		}
		for (Long del : keyList) {
			try {
				//Agregar excepcion para EA04 y EA05
				String fullBasePath = StringUtils.buildFilePath(basePath);
				SftpCierreAnualIMSS.createBaseFolder(fullBasePath);
				logger.info("Ruta donde se depositara el archivo {}", fullBasePath);
				String nombreArchivo = StringUtils.nombreArchivo(del);
				logger.info("Nombre del archivo {} para la key {}", nombreArchivo, del);
				sftpClient.uploadFile(StringUtils.buildFilePath(CierreAnualImssConstants.BASE_PATH_SERVER).concat(nombreArchivo), fullBasePath.concat(nombreArchivo));
				llavesCorrectas.add(del);
				fileControlRepository.createCorrect(action, del);
			} catch (Exception e) {
				exceptionKeyList.add(del);
				fileControlRepository.createError(action, del);
				logger.error("Error al subir el archivo para la key {}", del);
				logger.error("Error " + e.getMessage());
				logger.error("Error " + e.getStackTrace());

			}
		}
		if (exceptionKeyList.size() > 0) {
			processAuditRepository.createIncorrect(auditAction, null);
			emailService.sendEmailToOOADList(templateName, exceptionKeyList, llavesCorrectas);
		} else {
			processControlRepository.createCorrect(ProcessActionEnum.CIERRE_ANUAL_IMSS_PROCESS.getDesc(), null);
			emailService.sendEmailPasoDosTresCuatro(pasoCuatro);
		}
		sftpClient.disconnect();
		return RepeatStatus.FINISHED;
	}

}
