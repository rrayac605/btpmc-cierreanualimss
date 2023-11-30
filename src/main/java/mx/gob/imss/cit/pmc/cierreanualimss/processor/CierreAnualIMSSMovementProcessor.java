package mx.gob.imss.cit.pmc.cierreanualimss.processor;

import lombok.SneakyThrows;
import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.AseguradoDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.IncapacidadDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.MovementDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.PatronDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.NumberUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@StepScope
public class CierreAnualIMSSMovementProcessor implements ItemProcessor<MovementDTO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CierreAnualIMSSMovementProcessor.class);
    
    @Value("#{stepExecution}")
    private StepExecution stepExecution;
    
        
    @SneakyThrows
    @Override
    public String process(MovementDTO movementDTO) {
    	Long key = stepExecution.getJobParameters().getLong(CierreAnualImssConstants.KEY_PARAM);
    	boolean esReproceso = Boolean.TRUE.toString().equals(stepExecution.getJobParameters().getString(
    			CierreAnualImssConstants.REPROCESS_FLAG));
    	//Agregar exception para obtener información
        AseguradoDTO asegurado = movementDTO.getAseguradoDTO();
        PatronDTO patron = movementDTO.getPatronDTO();
        IncapacidadDTO incapacidad = movementDTO.getIncapacidadDTO();
        return StringUtils.safeSubString(asegurado.getNumNss(), CierreAnualImssConstants.TEN)
                .concat(StringUtils.safeSubString(patron.getRefRegistroPatronal(), CierreAnualImssConstants.TEN))
                .concat(NumberUtils.processConsequence(
                        incapacidad.getCveConsecuencia(), incapacidad.getNumDiasSubsidiados()).toString()
                )
                .concat(DateUtils.getFileFormattedDate(
                        DateUtils.orElse(incapacidad.getFecInicio(), incapacidad.getFecAccidente())
                ))
                .concat(StringUtils.safeAddZero(incapacidad.getNumDiasSubsidiados(), 4))
                .concat(DateUtils.getFileFormattedDate(
                        DateUtils.orElse(incapacidad.getFecFin(), incapacidad.getFecIniPension(), incapacidad.getFecAltaIncapacidad())
                ))
                .concat(NumberUtils.safeValidateInteger(incapacidad.getCveTipoRiesgo()).toString())
                .concat(StringUtils.safeAddZero(NumberUtils.safetyParseBigDecimal(incapacidad.getPorPorcentajeIncapacidad()), 3))
                .concat(StringUtils.concatFullNameFile(asegurado.getNomAsegurado(), asegurado.getRefPrimerApellido(), asegurado.getRefSegundoApellido()))
                .concat(StringUtils.safeAddZero(0, 46));
    }
}
