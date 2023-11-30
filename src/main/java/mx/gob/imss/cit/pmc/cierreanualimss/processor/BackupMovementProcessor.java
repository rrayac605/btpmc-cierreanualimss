package mx.gob.imss.cit.pmc.cierreanualimss.processor;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.SneakyThrows;
import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.AseguradoDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.AuditoriaDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.IncapacidadDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.MovementDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.PatronDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.RespaldoCierreAnualIMSSDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.NumberUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.StringUtils;

@Component
@StepScope
public class BackupMovementProcessor implements ItemProcessor<MovementDTO, RespaldoCierreAnualIMSSDTO> {
	
	private static final Logger logger = LoggerFactory.getLogger(BackupMovementProcessor.class);

    @Value("#{stepExecution}")
    private StepExecution stepExecution;
    
    @SneakyThrows
    @Override
    public RespaldoCierreAnualIMSSDTO process(MovementDTO movementDTO) {
        Long key = stepExecution.getJobParameters().getLong(CierreAnualImssConstants.KEY_PARAM_DEL);
        //Agregar exception para respaldar información
        RespaldoCierreAnualIMSSDTO respaldoCierreAnualImssDTO = new RespaldoCierreAnualIMSSDTO();
        AseguradoDTO asegurado = movementDTO.getAseguradoDTO();
        PatronDTO patron = movementDTO.getPatronDTO();
        IncapacidadDTO incapacidad = movementDTO.getIncapacidadDTO();
        
        List<AuditoriaDTO> auditoriasList = movementDTO.getAuditorias();
        AuditoriaDTO auditoria = new AuditoriaDTO();
        if (!CollectionUtils.isEmpty(auditoriasList)) {
        	auditoria = auditoriasList.get(auditoriasList.size() - 1 );
		}
        
        respaldoCierreAnualImssDTO.setObjectIdOrigen(movementDTO.getObjectId());
        respaldoCierreAnualImssDTO.setNss(asegurado.getNumNss());
        respaldoCierreAnualImssDTO.setOoadNSS(asegurado.getCveDelegacionNss());
        respaldoCierreAnualImssDTO.setSubDelegacionNSS(asegurado.getCveSubdelNss());
        respaldoCierreAnualImssDTO.setUmfNSS(asegurado.getCveUmfAdscripcion());
        respaldoCierreAnualImssDTO.setRegistroPatronal(patron.getRefRegistroPatronal());
        respaldoCierreAnualImssDTO.setOoadRegistroPatronal(patron.getCveDelRegPatronal());
        respaldoCierreAnualImssDTO.setSubDelRegistroPatronal(patron.getCveSubDelRegPatronal());
        respaldoCierreAnualImssDTO.setCveConsecuencia(NumberUtils.processConsequence(incapacidad.getCveConsecuencia(), incapacidad.getNumDiasSubsidiados()));
        respaldoCierreAnualImssDTO.setDescripcionConsecuencia(incapacidad.getDesConsecuencia());
        respaldoCierreAnualImssDTO.setFechaInicioAccidente(DateUtils.orElse(incapacidad.getFecInicio(), incapacidad.getFecAccidente()));
        respaldoCierreAnualImssDTO.setFechaFinTermino(DateUtils.orElse(incapacidad.getFecFin(), incapacidad.getFecIniPension(), incapacidad.getFecAltaIncapacidad()));
        respaldoCierreAnualImssDTO.setDiasSubsidiados(incapacidad.getNumDiasSubsidiados());
        respaldoCierreAnualImssDTO.setTipoRiesgo(NumberUtils.safeValidateInteger(incapacidad.getCveTipoRiesgo()));
        respaldoCierreAnualImssDTO.setPorcentajeIncapacidad(NumberUtils.safetyParseBigDecimal(incapacidad.getPorPorcentajeIncapacidad()));
        respaldoCierreAnualImssDTO.setNombreAsegurado(StringUtils.concatFullName(asegurado.getNomAsegurado(),
              asegurado.getRefPrimerApellido(), asegurado.getRefSegundoApellido(), CierreAnualImssConstants.EMPTY_SPACE));
        respaldoCierreAnualImssDTO.setCurpAsegurado(StringUtils.safeValidateCurp(asegurado.getRefCurp()));
        respaldoCierreAnualImssDTO.setAnioEjecucionRespaldo(Integer.parseInt(DateUtils.getCurrentYear()));
        respaldoCierreAnualImssDTO.setClaveEstadoRiesgo(asegurado.getCveEstadoRegistro());
        respaldoCierreAnualImssDTO.setDescripcionEstadoRiesgo(asegurado.getDesEstadoRegistro());
        respaldoCierreAnualImssDTO.setOrigen(movementDTO.getCveOrigenArchivo());
        respaldoCierreAnualImssDTO.setAnioRevision(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
        respaldoCierreAnualImssDTO.setRazonSocial(movementDTO.getPatronDTO().getDesRazonSocial());
        if (StringUtils.isNotEmpty(asegurado.getNumCicloAnual())) {
            respaldoCierreAnualImssDTO.setAnioCicloCaso(Integer.parseInt(asegurado.getNumCicloAnual()));
		}
        respaldoCierreAnualImssDTO.setClaveSituacionRiesgo(auditoria.getCveSituacionRegistro());
        respaldoCierreAnualImssDTO.setDescripcionSituacionRiesgo(auditoria.getDesSituacionRegistro());
        
        respaldoCierreAnualImssDTO.setFecRespaldo(new Date());
        
        respaldoCierreAnualImssDTO.setKey(key);

        return respaldoCierreAnualImssDTO;
    }

}
