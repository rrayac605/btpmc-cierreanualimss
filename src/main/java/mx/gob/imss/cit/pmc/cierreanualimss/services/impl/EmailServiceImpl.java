package mx.gob.imss.cit.pmc.cierreanualimss.services.impl;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.DiasSubsidiadosDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.EmailTemplateDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.PorcentajeIncapacidadDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.RespaldoCierreAnualIMSSDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.CountRepository;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.EmailTemplateRepository;
import mx.gob.imss.cit.pmc.cierreanualimss.services.EmailService;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.ReaderUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    
    @Autowired
    private MongoOperations mongoOperations;
    
    @Autowired
    private CountRepository countRepository;

    @Override
    public void sendEmail(String templateName, Set<Long> keyList) {
        try {
            EmailTemplateDTO emailTemplate = emailTemplateRepository.findByName(templateName);
            logger.info("Template a enviar -> {}", templateName);
            llenaExito(emailTemplate, keyList);
            MimeMessageHelper mimeMessageHelper = construyeCorreoExito(emailTemplate);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | MailException e) {
            logger.error("Error al enviar el email", e);
        }
    }
    
    @Override
    public void sendEmailGetBackupInfoFailedList(String templateName, Set<Long> keyList) {
        try {
            EmailTemplateDTO emailTemplate = emailTemplateRepository.findByName(templateName);
            logger.info("Template a enviar --> {}", templateName);
            if(templateName.equals(CierreAnualImssConstants.ERROR_OBTENER_INFO) ||
            		templateName.equals(CierreAnualImssConstants.ERROR_RESPALDO_INFO)) {
            	llenaGetBackupInfoFailed(emailTemplate);
            }
            MimeMessageHelper mimeMessageHelper = buildEmailMessageHelper(emailTemplate);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | MailException e) {
            logger.error("Error al enviar el email", e);
        }
    }

    @Override
    public void sendEmailToOOADList(String templateName, Set<Long> keyList, Set<Long> llaves) {
        try {
            EmailTemplateDTO emailTemplate = emailTemplateRepository.findByName(templateName);
            logger.info("Template a enviar --> {}", templateName);
            if(templateName.equals(CierreAnualImssConstants.ERROR_GENERAR_ARCHIVO)) {
            	llenaFileGenerationFailed(emailTemplate, keyList, llaves);
            } else if(templateName.equals(CierreAnualImssConstants.ERROR_ALMACENAR_ARCHIVO)) {
            	llenaUploadFilesFailed(emailTemplate, keyList, llaves);
            }
            MimeMessageHelper mimeMessageHelper = buildEmailMessageHelper(emailTemplate);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | MailException e) {
            logger.error("Error al enviar el email", e);
        }
    }
    
    public void sendEmailPasoUno(String templateName) {
    	try {
    		logger.info("Voy a enviar correo del paso 1");
    		EmailTemplateDTO emailTempalte = emailTemplateRepository.findByName(templateName);
    		llenaPasoUno(emailTempalte);
    		MimeMessageHelper mimeMessageHelper = buildEmailMessageHelper(emailTempalte);
    		javaMailSender.send(mimeMessageHelper.getMimeMessage());
    	} catch(MessagingException | MailException e) {
    		logger.error("Error al enviar el email ", e);
    	}
    }
    
    public void sendEmailPasoDosTresCuatro(String templateName) {
    	try {
    		logger.info("Voy a enviar correo del paso 2 - 3 - 4");
    		EmailTemplateDTO emailTemplate = emailTemplateRepository.findByName(templateName);
    		llenaStepsTwoThreeFour(emailTemplate);
    		MimeMessageHelper mimeMessageHelper = buildEmailMessageHelper(emailTemplate);
    		javaMailSender.send(mimeMessageHelper.getMimeMessage());
    	} catch(MessagingException | MailException e) {
    		logger.error("Error al enviar emal ", e);
    	}
    }

    private MimeMessageHelper buildEmailMessageHelper(EmailTemplateDTO emailTemplate) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE, CierreAnualImssConstants.ENCODING);
        mimeMessageHelper.setSubject(emailTemplate.getSubject());
        mimeMessageHelper.setFrom(emailTemplate.getFrom());
        mimeMessageHelper.setTo(emailTemplate.getTo());
        mimeMessageHelper.setText(emailTemplate.getTemplate(), Boolean.TRUE);
        mimeMessageHelper.addInline(CierreAnualImssConstants.HEADER_IMG,
                new ClassPathResource(CierreAnualImssConstants.IMAGE_PATH.concat(CierreAnualImssConstants.HEADER_IMG)));
        mimeMessageHelper.addInline(CierreAnualImssConstants.FOOTER_IMG,
                new ClassPathResource(CierreAnualImssConstants.IMAGE_PATH.concat(CierreAnualImssConstants.FOOTER_IMG)));
        return mimeMessageHelper;
    }
    
    private MimeMessageHelper construyeCorreoExito(EmailTemplateDTO emailTemplate) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE, CierreAnualImssConstants.ENCODING);
        mimeMessageHelper.setSubject(emailTemplate.getSubject());
        mimeMessageHelper.setFrom(emailTemplate.getFrom());
        mimeMessageHelper.setTo(emailTemplate.getTo());
        mimeMessageHelper.setText(emailTemplate.getTemplate(), Boolean.TRUE);
        mimeMessageHelper.addInline(CierreAnualImssConstants.HEADER_IMG_ESP,
                new ClassPathResource(CierreAnualImssConstants.IMAGE_PATH.concat(CierreAnualImssConstants.HEADER_IMG_ESP)));
        mimeMessageHelper.addInline(CierreAnualImssConstants.FOOTER_IMG_ESP,
                new ClassPathResource(CierreAnualImssConstants.IMAGE_PATH.concat(CierreAnualImssConstants.FOOTER_IMG_ESP)));
        return mimeMessageHelper;
    }

    private void llenaExito(EmailTemplateDTO emailTemplateDTO, Set<Long> keyList) {
    	logger.info("Entre al exito");
    	
    	DecimalFormat df = new DecimalFormat("###,###");
    	
    	TypedAggregation<RespaldoCierreAnualIMSSDTO> registrosEnviados = ReaderUtils.totalRiesgos();
    	
    	String diaEjec = DateUtils.obtenerDiaEjecucion(new Date());
    	String diaFin = DateUtils.obtenerDiaFin();
    	String horaFin = DateUtils.obtenerHoraFin();
    	String anioRev = String.valueOf(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
    	String ooads = "";
    	String totales = "";
    	
    	long totalRegsEnviados = countRepository.count(registrosEnviados);
    	long totalCorrectos = 0L;
    	long totalCorrectosOtras = 0L;
    	long totalSusceptibles = 0L;
    	long totalSusceptiblesOtras = 0L;
    	long totalDefunsiones = 0L;
    	long totalPorcentajes = 0L;
    	long totalDias = 0L;
    	
    	for(Long key : keyList) {
    		
    		TypedAggregation<RespaldoCierreAnualIMSSDTO> cuentaCorrectos = ReaderUtils.contarCorrectos(key);
    		TypedAggregation<RespaldoCierreAnualIMSSDTO> cuentaCorrectosOtras = ReaderUtils.contarCorrectosOtras(key);
    		TypedAggregation<RespaldoCierreAnualIMSSDTO> cuentaSusceptibles = ReaderUtils.contarSusceptibles(key);
    		TypedAggregation<RespaldoCierreAnualIMSSDTO> cuentaSusceptiblesOtras = ReaderUtils.contarSusceptiblesOtras(key);
    		TypedAggregation<RespaldoCierreAnualIMSSDTO> cuentaDefunciones = ReaderUtils.contarDefunciones(key);
    		TypedAggregation<RespaldoCierreAnualIMSSDTO> sumarPorcentajes = ReaderUtils.sumarPorcentajes(key);
    		TypedAggregation<RespaldoCierreAnualIMSSDTO> sumarDias = ReaderUtils.sumarDias(key);
    		
    		AggregationResults<PorcentajeIncapacidadDTO> aggregationPorcentajes = mongoOperations.aggregate(sumarPorcentajes, PorcentajeIncapacidadDTO.class);
    		AggregationResults<DiasSubsidiadosDTO> aggregationDias = mongoOperations.aggregate(sumarDias, DiasSubsidiadosDTO.class);
    		
    		List<PorcentajeIncapacidadDTO> listaPorcentajes = aggregationPorcentajes.getMappedResults();
    		List<DiasSubsidiadosDTO> listaDias = aggregationDias.getMappedResults();
    		
    		long correctos = countRepository.count(cuentaCorrectos);
    		long correctosOtras = countRepository.count(cuentaCorrectosOtras);
    		long susceptibles = countRepository.count(cuentaSusceptibles);
    		long susceptiblesOtras = countRepository.count(cuentaSusceptiblesOtras);
    		long defunciones = countRepository.count(cuentaDefunciones);
    		long porcentajes = listaPorcentajes.size() > 0 ? listaPorcentajes.get(0).getPorcentajeIncapacidad() : 0L;
    		long dias = listaDias.size() > 0 ? listaDias.get(0).getDiasSubsidiados() : 0L;
    		
    		   			
    		ooads = ooads.concat("<tr id='trOOAD'>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(CierreAnualImssConstants.DEL_DESCRIPTION_MAP.get(key)).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(correctos))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(correctosOtras))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(susceptibles))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(susceptiblesOtras))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(defunciones))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(porcentajes))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(dias))).concat("</td>");
    		ooads = ooads.concat("</tr>");
    		
    		totalCorrectos+=correctos;
    		totalCorrectosOtras+=correctosOtras;
    		totalSusceptibles+=susceptibles;
    		totalSusceptiblesOtras+=susceptiblesOtras;
    		totalDefunsiones+=defunciones;
    		totalPorcentajes+=porcentajes;
    		totalDias+=dias;
    		
    	}
   				
		totales = totales.concat("<td style='border:1px solid black'>").concat("TOTALES").concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalCorrectos))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalCorrectosOtras))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalSusceptibles))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalSusceptiblesOtras))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalDefunsiones))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalPorcentajes))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalDias))).concat("</td>");
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.DIA_EJECUCION, diaEjec));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.DIA_FIN, diaFin));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.HORA_FIN, horaFin));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.ANIO_REV, anioRev));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.TOTAL_REGS, String.valueOf(df.format(totalRegsEnviados))));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.LISTA_OOAD, ooads));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.TOTALES_CIERRE_IMSS, totales));
    	
    }
    
    private void llenaPasoUno(EmailTemplateDTO emailTemplateDTO) {
    	logger.info("Entre al paso 1");
    	
    	DecimalFormat df = new DecimalFormat("###,###");
    	    	
    	TypedAggregation<RespaldoCierreAnualIMSSDTO> riesgos = ReaderUtils.totalRiesgos();
    	TypedAggregation<RespaldoCierreAnualIMSSDTO> defunciones = ReaderUtils.contarTotalDefunciones();
    	TypedAggregation<RespaldoCierreAnualIMSSDTO> porcentaje = ReaderUtils.sumarTotalPorcentajes();
    	TypedAggregation<RespaldoCierreAnualIMSSDTO> dias = ReaderUtils.sumarTotalDias();
    	TypedAggregation<RespaldoCierreAnualIMSSDTO> correctos = ReaderUtils.contarTotalCorrectos();
    	TypedAggregation<RespaldoCierreAnualIMSSDTO> correctosOtras = ReaderUtils.contarTotalCorrectosOtras();
    	TypedAggregation<RespaldoCierreAnualIMSSDTO> susceptibles = ReaderUtils.contarTotalSusceptibles();
    	TypedAggregation<RespaldoCierreAnualIMSSDTO> susceptiblesOtras = ReaderUtils.contarTotalSusceptiblesOtras();
    	
    	AggregationResults<PorcentajeIncapacidadDTO> aggregationPorcentaje = mongoOperations.aggregate(porcentaje, PorcentajeIncapacidadDTO.class);
    	AggregationResults<DiasSubsidiadosDTO> aggregationDias = mongoOperations.aggregate(dias, DiasSubsidiadosDTO.class);
    	
    	List<PorcentajeIncapacidadDTO> listaPorcentaje = aggregationPorcentaje.getMappedResults();
    	List<DiasSubsidiadosDTO> listaDias = aggregationDias.getMappedResults();
    	
    	long totRiesgos = countRepository.count(riesgos);
    	long totDefunciones = countRepository.count(defunciones);
    	long totPorcentajes = listaPorcentaje.size() > 0 ? listaPorcentaje.get(0).getPorcentajeIncapacidad() : 0L;
    	long totDias = listaDias.size() > 0 ? listaDias.get(0).getDiasSubsidiados() : 0L;
    	long totCorrectos = countRepository.count(correctos);
    	long totCorrectosOtras = countRepository.count(correctosOtras);
    	long totSus = countRepository.count(susceptibles);
    	long totSusOtras = countRepository.count(susceptiblesOtras);
    	
    	String anioRev = String.valueOf(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
    	String totalRiesgos = "";
    	String totalDefunciones = "";
    	String totalPorcentajes = "";
    	String totalDias = "";
    	String totalCorrectos = "";
    	String totalCorrectosOtras = "";
    	String totalSus = "";
    	String totalSusOtras = "";
    	
    	totalRiesgos = totalRiesgos.concat("Total de riesgos: ").concat(String.valueOf(df.format(totRiesgos)));
    	totalDefunciones = totalDefunciones.concat("Defunciones: ").concat(String.valueOf(df.format(totDefunciones)));
    	totalPorcentajes = totalPorcentajes.concat("Porcentaje de incapacidad: ").concat(String.valueOf(df.format(totPorcentajes)));
    	totalDias = totalDias.concat("D&iacute;as subsidiados: ").concat(String.valueOf(df.format(totDias)));
    	totalCorrectos = totalCorrectos.concat("Correctos: ").concat(String.valueOf(df.format(totCorrectos)));
    	totalCorrectosOtras = totalCorrectosOtras.concat("Correctos otras delegaciones: ").concat(String.valueOf(df.format(totCorrectosOtras)));
    	totalSus = totalSus.concat("Susceptibles de ajuste: ").concat(String.valueOf(df.format(totSus)));
    	totalSusOtras = totalSusOtras.concat("Susceptible de ajuste otras delegaciones: ").concat(String.valueOf(df.format(totSusOtras)));
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.ANIO_REV, anioRev));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.TOTAL_RIESGOS, totalRiesgos));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.DEFUNCIONES, totalDefunciones));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.PORCENTAJES, totalPorcentajes));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.DIAS, totalDias));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.CORRECTOS, totalCorrectos));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.CORRECTOS_OTRAS, totalCorrectosOtras));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.SUS, totalSus));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.SUS_OTRAS, totalSusOtras));
    }
    
    private void llenaGetBackupInfoFailed(EmailTemplateDTO emailTemplateDTO) {
    	logger.info("Entre a get info failed");
    	
    	String diaEjec = DateUtils.obtenerDiaEjecucion(new Date());
    	String horaEjec = DateUtils.obtenerHoraEjec();
    	String diaSig = DateUtils.obtenerDiaSig(new Date());
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.DIA_EJECUCION, diaEjec));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.HORA_EJECUCION, horaEjec));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.DIA_SIG, diaSig));
    }
    
    private void llenaFileGenerationFailed(EmailTemplateDTO emailTemplateDTO, Set<Long> keyList, Set<Long> llaves) {
    	logger.info("Entre a file generation failed");
    	
    	String diaEjecucion = DateUtils.obtenerDiaEjecucion(new Date());
    	String horaEjecucion = DateUtils.obtenerHoraEjec();
    	String diaSiguiente = DateUtils.obtenerDiaSig(new Date());
    	String anioRev = String.valueOf(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
    	
    	String ooadError = "";
    	String ooadExito = "";
    	
    	for (Long key : keyList) {
    		ooadError = ooadError.concat("<p>")
                    .concat(CierreAnualImssConstants.DEL_DESCRIPTION_MAP.get(key))
                    .concat("</p>");
		}
    	
    	for(Long key : llaves) {
    		ooadExito = ooadExito.concat("<p>")
    				.concat(CierreAnualImssConstants.DEL_DESCRIPTION_MAP.get(key))
    				.concat("</p>");
    	}
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.DIA_EJECUCION, diaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.HORA_EJECUCION, horaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.DIA_SIG, diaSiguiente));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.ANIO_REV, anioRev));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.OOAD_ERROR, ooadError));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.OOAD_EXITO, ooadExito));
    }
    
    private void llenaUploadFilesFailed(EmailTemplateDTO emailTemplateDTO, Set<Long> keyList, Set<Long> llaves) {
    	logger.info("Entre a update files failed");
    	
    	String diaEjecucion = DateUtils.obtenerDiaEjecucion(new Date());
    	String horaEjecucion = DateUtils.obtenerHoraEjec();
    	String diaSiguiente = DateUtils.obtenerDiaSig(new Date());
    	String anioRev = String.valueOf(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
    	
    	String ooadError = "";
    	String ooadExito = "";
    	
    	for(Long key : keyList) {
    		ooadError = ooadError.concat("<p>")
    				.concat(CierreAnualImssConstants.DEL_DESCRIPTION_MAP.get(key))
    				.concat("</p>");
    	}
    	
    	for(Long key : llaves) {
    		ooadExito = ooadExito.concat("<p>")
    				.concat(CierreAnualImssConstants.DEL_DESCRIPTION_MAP.get(key))
    				.concat("</p>");
    	}
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.DIA_EJECUCION, diaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.HORA_EJECUCION, horaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.DIA_SIG, diaSiguiente));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.ANIO_REV, anioRev));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.OOAD_ERROR, ooadError));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.OOAD_EXITO, ooadExito));
    }
    
    private void llenaStepsTwoThreeFour(EmailTemplateDTO emailTemplateDTO) {
    	logger.info("Entre a steps 2-3-4");
    	
    	String anioRev = String.valueOf(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(CierreAnualImssConstants.ANIO_REV, anioRev));
    }

}
