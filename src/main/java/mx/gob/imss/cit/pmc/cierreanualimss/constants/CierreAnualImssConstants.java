package mx.gob.imss.cit.pmc.cierreanualimss.constants;

import com.google.common.collect.ImmutableMap;

import mx.gob.imss.cit.pmc.cierreanualimss.enums.AuditActionEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.ProcessActionEnum;

import org.springframework.batch.core.StepExecution;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CierreAnualImssConstants {

    public static final String CTROL_LLAVE_CTL = "000000000000000000000000000";
    
    public static final String CTROL_HEADER = "HEADER                             ";
    
    public static final String CTROL_CASUI = "CASUI99";
    
    public static final String CTROL_NOMB_ARCH_CTL = "TSRT-A-CSQ";
    
    public static final String CTROL_CVE_CTL = "000";
    
    public static final String CTROL_TOTAL_REG_CTL = "0000000000";
    
    public static final String FILLER = "0000000000000000000000000000";

    public static final String KEY_PARAM = "key";
    
    public static final String KEY_PARAM_DEL = "delegacion";
    
    public static final String KEY_PARAM_SUBDEL = "subDelegacion";

    public static final String NOT_ERROR_FLAG_PARAM = "not_error_flag";

    public static final String REPROCESS_FLAG = "reprocess_flag";

    public static final String OOAD_LIST_TEMPLATE = "${ooadList}";

    public static final String ACTION_PARAM = "action";

    public static final String ACTION_AUDIT_PARAM = "action_audit";

    public static final String PAST_STEP_PARAM = "past_step";

    public static final String IS_TERMINATED_ONLY = "is_terminated_only";

    public static final String EXECUTION_DATE = "execution_date";

    public static final String CIERREANUALIMSS_JOB = "cierreAnualIMSSJob";

    public static final String CIERREANUALIMSS_NEXT_JOB = "cierreAnualIMSSNextJob";

    public static final String FILE_VALIDATION_STEP = "fileValidationStep";

    public static final String SFTP_UPLOAD_FILE_STEP = "sftpUploadFileStep";

    public static final String HEADER_STEP = "headerStep";

    public static final String FOOTER_STEP = "footerStep";

    public static final Long WILL_BE_INJECTED_LONG = null;

    public static final String WILL_BE_INJECTED_STRING = null;

    public static final StepExecution WILL_BE_INJECTED_SE = null;

    public static final String PATTERN_YYYYMMDD = "yyyyMMdd";

    public static final String PATTERN_TIME = "HHmmss";

    public static final String BASE_PATH_SERVER = "/opt/jboss/archivoscierreanualRFCIMSS/respaldo/{year}/{month}/";

    public static final String FILE_NAME = "CASORI";
    
    public static final String FILE_EXTENSION = ".txt";
    
    public static final String USER = "Sistema PMC";

    public static final String ENCODING = "UTF-8";

    public static final String FOOTER_IMG = "footer.png";

    public static final String HEADER_IMG = "header.png";
    
    public static final String FOOTER_IMG_ESP = "footerEsp.png";

    public static final String HEADER_IMG_ESP = "headerEsp.png";

    public static final String IMAGE_PATH = "/static/images/";

    public static final String RFC_IMSS = "IMS421231I45";
    
    public static final String ERROR_OBTENER_INFO = "CIERRE_IMSS_ERROR_OBTENER_INFO";
    
    public static final String ERROR_RESPALDO_INFO = "CIERRE_IMSS_ERROR_RESPALDO_INFO";
    
    public static final String ERROR_GENERAR_ARCHIVO = "CIERRE_IMSS_ERROR_GENERAR_ARCHIVO";
    
    public static final String ERROR_ALMACENAR_ARCHIVO = "CIERRE_IMSS_ERROR_ALMACENAR_ARCHIVO";
    
    public static final String PASO_UNO = "CIERRE_IMSS_PASO_UNO";
    
    public static final String PASO_DOS = "CIERRE_IMSS_PASO_DOS";
    
    public static final String PASO_TRES = "CIERRE_IMSS_PASO_TRES";
    
    public static final String PASO_CUATRO = "CIERRE_IMSS_PASO_CUATRO";
    
    public static final String FORMATO_DIA_EJECUCION = "dd-MM-YYYY";

	public static final String FORMATO_HORA_EJEC = "HH:mm";
	
	public static final String FORMATO_HORA = "HH:mm:ss";
	
	public static final String ULTIMA_HORA_DIA = "23:59:59";
    
    public static final String DIA_EJECUCION = "${diaEjecucion}";
    
    public static final String HORA_EJECUCION = "${horaEjecucion}";
    
    public static final String DIA_SIG = "${diaSiguiente}";
    
    public static final String ANIO_REV = "${anioRevision}";
    
    public static final String OOAD_ERROR = "${ooadError}";
    
    public static final String OOAD_EXITO = "${ooadExito}";
    
    public static final String TOTAL_RIESGOS = "${totalRiesgos}";

	public static final String DEFUNCIONES = "${defunciones}";

	public static final String PORCENTAJES = "${porcentajeIncapacidad}";

	public static final String DIAS = "${diasSubsidiados}";

	public static final String CORRECTOS = "${correctos}";

	public static final String CORRECTOS_OTRAS = "${correctosOtras}";

	public static final String SUS = "${susceptibles}";

	public static final String SUS_OTRAS = "${susceptiblesOtras}";
	
	public static final String DIA_FIN = "${diaFin}";
	
	public static final String HORA_FIN = "${horaFin}";
	
	public static final String TOTAL_REGS = "${totalRegs}";

	public static final String LISTA_OOAD = "${listaOOADS}";
	
	public static final String TOTALES_CIERRE_IMSS = "${totales}";
    
    public static final int CVE_CONSECUENCIA_DEFUNCION = 4;
    
    public static final int CVE_CORRECTOS = 1;
    
    public static final int CVE_CORRECTOS_OTRAS = 5; 
    
    public static final int CVE_SUSCEPTIBLES = 4;
    
    public static final int CVE_SUSCEPTIBLES_OTRAS = 8;

    public static final String MES_SEPTIEMBRE = "septiembre";
    
    public static final Map<Integer, List<Integer>> DEL_SUBDEL = ImmutableMap.<Integer, List<Integer>>builder()
            .put(1, Arrays.asList(1, 1, 19))
            .put(2, Arrays.asList(2, 1, 2, 3, 4))
            .put(3, Arrays.asList(3, 1, 8))
            .put(4, Arrays.asList(4, 1, 4))
            .put(5, Arrays.asList(8, 1, 3, 5, 8, 22, 60))
            .put(6, Arrays.asList(8, 10))
            .put(7, Arrays.asList(6, 1, 3, 7))
            .put(8, Arrays.asList(5, 3, 11, 12, 17, 23))
            .put(9, Arrays.asList(7, 1, 2))
            .put(10, Arrays.asList(39, 11, 16, 54, 56, 57))
            .put(11, Arrays.asList(40, 1, 6, 11, 54, 58))
            .put(12, Arrays.asList(10, 1, 13))
            .put(13, Arrays.asList(12, 1, 2, 3, 13))
            .put(14, Arrays.asList(11, 1, 5, 8, 14, 17))
            .put(15, Arrays.asList(27, 1))
            .put(16, Arrays.asList(13, 1, 5, 7, 10))
            .put(17, Arrays.asList(14, 12, 15, 22, 38, 39, 40, 50))
            .put(18, Arrays.asList(17, 3, 9, 13, 17, 27))
            .put(19, Arrays.asList(16, 1, 5))
            .put(20, Arrays.asList(20, 6, 8, 31, 32, 33, 34))
            .put(21, Arrays.asList(18, 1, 11, 15))
            .put(22, Arrays.asList(29, 19))
            .put(23, Arrays.asList(26, 5))
            .put(24, Arrays.asList(19, 1))
            .put(25, Arrays.asList(21, 2, 3, 4, 53))
            .put(26, Arrays.asList(22, 1, 5, 6, 8, 22))
            .put(27, Arrays.asList(24, 1, 2, 7))
            .put(28, Arrays.asList(23, 1, 3))
            .put(29, Arrays.asList(26, 1, 3, 4))
            .put(30, Arrays.asList(25, 1, 3, 5, 60))
            .put(31, Arrays.asList(27, 3, 7, 10, 13, 51, 57, 70))
            .put(32, Arrays.asList(29, 1, 4, 10, 13, 18))
            .put(33, Arrays.asList(28, 1, 2))
            .put(34, Arrays.asList(2, 5))
            .put(35, Arrays.asList(15, 6, 54, 80))
            .put(36, Arrays.asList(5, 9))
            .put(37, Arrays.asList(30, 1))
            .put(38, Arrays.asList(31, 2, 7, 9, 25))
            .put(39, Arrays.asList(31, 12))
            .put(40, Arrays.asList(32, 2, 3, 38, 45))
            .put(41, Arrays.asList(33, 1, 33))
            .put(42, Arrays.asList(34, 1, 9))
            .build();
    
    public static final Map<Integer, List<Integer>> DEL_SUBDEL_NOMBRE = ImmutableMap.<Integer, List<Integer>>builder()
          .put(1, Arrays.asList(1, 0))
          .put(2, Arrays.asList(2, 0))
          .put(3, Arrays.asList(3, 0))
          .put(4, Arrays.asList(4, 0))
          .put(5, Arrays.asList(8, 0))
          .put(6, Arrays.asList(8, 10))
          .put(7, Arrays.asList(6, 0))
          .put(8, Arrays.asList(5, 0))
          .put(9, Arrays.asList(7, 0))
          .put(10, Arrays.asList(39, 0))
          .put(11, Arrays.asList(40, 0))
          .put(12, Arrays.asList(10, 0))
          .put(13, Arrays.asList(12, 0))
          .put(14, Arrays.asList(11, 0))
          .put(15, Arrays.asList(27, 1))
          .put(16, Arrays.asList(13, 0))
          .put(17, Arrays.asList(14, 0))
          .put(18, Arrays.asList(17, 0))
          .put(19, Arrays.asList(16, 0))
          .put(20, Arrays.asList(20, 0))
          .put(21, Arrays.asList(18, 0))
          .put(22, Arrays.asList(29, 19))
          .put(23, Arrays.asList(26, 5))
          .put(24, Arrays.asList(19, 0))
          .put(25, Arrays.asList(21, 0))
          .put(26, Arrays.asList(22, 0))
          .put(27, Arrays.asList(24, 0))
          .put(28, Arrays.asList(23, 0))
          .put(29, Arrays.asList(26, 0))
          .put(30, Arrays.asList(25, 0))
          .put(31, Arrays.asList(27, 0))
          .put(32, Arrays.asList(29, 0))
          .put(33, Arrays.asList(28, 0))
          .put(34, Arrays.asList(2, 5))
          .put(35, Arrays.asList(15, 0))
          .put(36, Arrays.asList(5, 9))
          .put(37, Arrays.asList(30, 0))
          .put(38, Arrays.asList(31, 0))
          .put(39, Arrays.asList(31, 12))
          .put(40, Arrays.asList(32, 0))
          .put(41, Arrays.asList(33, 0))
          .put(42, Arrays.asList(34, 0))
          .build();

    public static final Map<Long, String> DEL_DESCRIPTION_MAP = ImmutableMap.<Long, String>builder()
            .put(1L, "AGUASCALIENTES")
            .put(2L, "BAJA CALIFORNIA NORTE")
            .put(3L, "BAJA CALIFORNIA SUR")
            .put(4L, "CAMPECHE")
            .put(5L, "CHIHUAHUA")
            .put(6L, "JUAREZ 1")
            .put(7L, "COLIMA")
            .put(8L, "COAHUILA")
            .put(9L, "CHIAPAS")
            .put(10L, "DISTRITO FEDERAL NORTE")
            .put(11L, "DISTRITO FEDERAL SUR")
            .put(12L, "DURANGO")
            .put(13L, "GUERRERO")
            .put(14L, "GUANAJUATO")
            .put(15L, "HERMOSILLO")
            .put(16L, "HIDALGO")
            .put(17L, "JALISCO")
            .put(18L, "MICHOACAN")
            .put(19L, "ESTADO DE MEXICO PONIENTE")
            .put(20L, "NUEVO LEON")
            .put(21L, "MORELOS")
            .put(22L, "MATAMOROS")
            .put(23L, "MAZATLAN")
            .put(24L, "NAYARIT")
            .put(25L, "OAXACA")
            .put(26L, "PUEBLA")
            .put(27L, "QUINTANA ROO")
            .put(28L, "QUERETARO")
            .put(29L, "SINALOA")
            .put(30L, "SAN LUIS POTOSI")
            .put(31L, "SONORA")
            .put(32L, "TAMAULIPAS")
            .put(33L, "TABASCO")
            .put(34L, "TIJUANA")
            .put(35L, "ESTADO DE MEXICO ORIENTE")
            .put(36L, "TORREON")
            .put(37L, "TLAXCALA")
            .put(38L, "VERACRUZ NORTE")
            .put(39L, "VERACRUZ PUERTO")
            .put(40L, "VERACRUZ SUR")
            .put(41L, "YUCATAN")
            .put(42L, "ZACATECAS")
            .build();
    
    
	public static final Map<String, String> FROM_STEP_ACTION_NAME = ImmutableMap.<String, String>builder()
            .put("movementStep", ProcessActionEnum.GET_INFO.getDesc())
            .put("changeStep", ProcessActionEnum.GET_INFO.getDesc())
            .put("backupMovementStep", ProcessActionEnum.BACKUP_INFO.getDesc())
            .put("backupChangeStep", ProcessActionEnum.BACKUP_INFO.getDesc())
            .put("backupMovimientosDescartadostStep", ProcessActionEnum.BACKUP_INFO.getDesc())
            .put("backupCambiosDescartadosStep", ProcessActionEnum.BACKUP_INFO.getDesc())
            .put(HEADER_STEP, ProcessActionEnum.GET_INFO.getDesc())
            .put(FOOTER_STEP, ProcessActionEnum.GET_INFO.getDesc())
            .put(FILE_VALIDATION_STEP, ProcessActionEnum.GENERATE_FILES.getDesc())
            .put(SFTP_UPLOAD_FILE_STEP, ProcessActionEnum.FILES_STORAGE.getDesc())
            .put("successProcessValidationStep", ProcessActionEnum.CIERRE_ANUAL_IMSS_PROCESS.getDesc())
            .build();

    public static final Map<String, String> FROM_STEP_FAILED_ACTION_NAME = ImmutableMap.<String, String>builder()
            .put("movementStep", ProcessActionEnum.GET_INFO.getDesc())
            .put("changeStep", ProcessActionEnum.GET_INFO.getDesc())
            .put(HEADER_STEP, ProcessActionEnum.FILE_GENERATION.getDesc())
            .put(FOOTER_STEP, ProcessActionEnum.FILE_GENERATION.getDesc())
            .put("backupMovementStep", ProcessActionEnum.BACKUP_INFO.getDesc())
            .put("backupChangeStep", ProcessActionEnum.BACKUP_INFO.getDesc())
            .put("backupMovimientosDescartadostStep", ProcessActionEnum.BACKUP_INFO.getDesc())
            .put("backupCambiosDescartadosStep", ProcessActionEnum.BACKUP_INFO.getDesc())
            .put(FILE_VALIDATION_STEP, ProcessActionEnum.GENERATE_FILES.getDesc())
            .put(SFTP_UPLOAD_FILE_STEP, ProcessActionEnum.FILES_STORAGE.getDesc())
            .build();

    public static final Map<String, String> FROM_STEP_ACTION_AUDIT_NAME = ImmutableMap.<String, String>builder()
            .put("movementStep", AuditActionEnum.OBTENER_INFO.getDesc())
            .put("changeStep", AuditActionEnum.OBTENER_INFO.getDesc())
            .put("backupMovementStep", AuditActionEnum.RESPALDAR_INFO.getDesc())
            .put("backupChangeStep", AuditActionEnum.RESPALDAR_INFO.getDesc())
            .put("backupMovimientosDescartadostStep", AuditActionEnum.RESPALDAR_INFO.getDesc())
            .put("backupCambiosDescartadosStep", AuditActionEnum.RESPALDAR_INFO.getDesc())
            .put("fileValidationStep", AuditActionEnum.ALMACENAR_ARCHIVOS.getDesc())
            .put(HEADER_STEP, AuditActionEnum.OBTENER_INFO.getDesc())
            .put(FOOTER_STEP, AuditActionEnum.OBTENER_INFO.getDesc())
            .put(SFTP_UPLOAD_FILE_STEP, AuditActionEnum.ALMACENAR_ARCHIVOS.getDesc())
            .build();

    public static final Map<String, String> FROM_STEP_FAILED_ACTION_AUDIT_NAME = ImmutableMap.<String, String>builder()
            .put("movementStep", AuditActionEnum.OBTENER_INFO.getDesc())
            .put("changeStep", AuditActionEnum.OBTENER_INFO.getDesc())
            .put(FOOTER_STEP, AuditActionEnum.GENERAR_ARCHIVOS.getDesc())
            .put(HEADER_STEP, AuditActionEnum.GENERAR_ARCHIVOS.getDesc())
            .put("backupMovementStep", AuditActionEnum.RESPALDAR_INFO.getDesc())
            .put("backupChangeStep", AuditActionEnum.RESPALDAR_INFO.getDesc())
            .put("backupMovimientosDescartadostStep", AuditActionEnum.RESPALDAR_INFO.getDesc())
            .put("backupCambiosDescartadosStep", AuditActionEnum.RESPALDAR_INFO.getDesc())
            .put(FILE_VALIDATION_STEP, AuditActionEnum.GENERAR_ARCHIVOS.getDesc())
            .put(SFTP_UPLOAD_FILE_STEP, AuditActionEnum.ALMACENAR_ARCHIVOS.getDesc())
            .build();

    public static final Map<String, String> FROM_STEP_TEMPLATE_NAME = ImmutableMap.<String, String>builder()
    		.put(FILE_VALIDATION_STEP, "CIERRE_IMSS_EXITO")
            .build();

    public static final Map<String, String> ACTION_TO_TEMPLATE_NAME = ImmutableMap.<String, String>builder()
            .put(ProcessActionEnum.GET_INFO.getDesc(), "CIERRE_IMSS_ERROR_OBTENER_INFO")
            .put(ProcessActionEnum.BACKUP_INFO.getDesc(), "CIERRE_IMSS_ERROR_RESPALDO_INFO")
            .put(ProcessActionEnum.FILE_GENERATION.getDesc(), "CIERRE_IMSS_ERROR_GENERAR_ARCHIVO")
            .build();

    public static final Map<Integer, String> FILE_VALIDATION_EQUIVALENT = ImmutableMap.<Integer, String>builder()
            .put(1, FILE_VALIDATION_STEP)
            .put(2, SFTP_UPLOAD_FILE_STEP)
            .build();

    public static final Map<Integer, String> UPLOAD_FILE_EQUIVALENT = ImmutableMap.<Integer, String>builder()
            .put(1, SFTP_UPLOAD_FILE_STEP)
            .build();

    public static final String PATTERN_DDMMYYYY_TIME = "ddMMyyyy HH:mm:ss";

    public static final String PATTERN_YYYY_MM_DD_TIME = "yyyy-MM-dd";

    public static final Integer CHUNK_SIZE = 300;

    public static final String MANUAL = "MN";

    public static final Integer POOL_SIZE = 64;

    public static final String MISSING_CURP = "000000000000000000";

    public static final String MISSING_NSS_RP = "0000000000";

    public static final Integer[] CRITERIA_ESTADO_REGISTRO_FULL = { 1, 5, 4, 8 };
    
    public static final Integer[] CRITERIA_ESTADO_REGISTRO_CORRECTO = { 1 };
    
    public static final Integer[] CRITERIA_ESTADO_REGISTRO_CORRECTO_OTRAS_DEL = { 5 };

    public static final Integer[] CRITERIA_ESTADO_REGISTRO_SUSCEPTIBLE = { 4 };
    
    public static final Integer[] CRITERIA_ESTADO_REGISTRO_SUSCEPTIBLE_OTRAS_DEL = { 8 };

    public static final Integer[] CRITERIA_ESTADO_REGISTRO_CORRECTO_FULL = { 1, 5 };

    public static final Integer[] CRITERIA_ESTADO_REGISTRO_SUSCEPTIBLE_FULL = { 4, 8 };

    public static final Integer[] CRITERIA_CONSECUENCIA = { null, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    
    public static final Integer[] CRITERIA_CONSECUENCIA_DEFUNCION = { 4 };

    public static final Integer CRITERIA_SITUACION_REGISTRO = 1;

    public static final Integer FIRST = 1;

    public static final Integer FIFTEEN = 15;

    public static final Integer ZERO = 0;

    public static final String ZERO_STRING = "0";

    public static final String FIRST_DAY = "1";

    public static final String FIRST_MONTH = "1";

    public static final String LAST_MONTH = "12";

    public static final String LAST_DECEMBER_DAY = "31";

    public static final String BEGIN_HOURS = " 00:00:00";

    public static final String END_HOURS = " 18:59:59";

    public static final String ISO_TIMEZONE = "UTC";

    public static final Integer HOURS_TO_ADD = 6;

    public static final Integer TEN = 10;

    public static final String EMPTY = "";

    public static final String EMPTY_SPACE = " ";

    public static final Map<String, Sort.Direction> READER_SORTER = ImmutableMap.of("_id", Sort.Direction.ASC);

    public static final Map<Integer, Integer> CONSEQUENCE_EQUIVALENCE = ImmutableMap.<Integer, Integer>builder()
            .put(0, 1)
            .put(10, 2)
            .put(11, 3)
            .put(12, 3)
            .build();

}
