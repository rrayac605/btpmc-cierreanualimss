package mx.gob.imss.cit.pmc.cierreanualimss.processor;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.ChangeDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.NumberUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class CierreAnualIMSSChangeProcessor implements ItemProcessor<ChangeDTO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CierreAnualIMSSMovementProcessor.class);

    @Override
    public String process(ChangeDTO changeDTO) {
        return StringUtils.safeSubString(changeDTO.getNumNss(), CierreAnualImssConstants.TEN)
                .concat(StringUtils.safeSubString(changeDTO.getRefRegistroPatronal(), CierreAnualImssConstants.TEN))
                .concat(NumberUtils.processConsequence(
                        changeDTO.getCveConsecuencia(), changeDTO.getNumDiasSubsidiados()).toString()
                )
                .concat(DateUtils.getFileFormattedDateChanges( 
                		DateUtils.orElse(changeDTO.getFecInicio(), changeDTO.getFecAccidente())
                ))
                .concat(StringUtils.safeAddZero(changeDTO.getNumDiasSubsidiados(), 4))
                .concat(DateUtils.getFileFormattedDateChanges(
                		DateUtils.orElse(changeDTO.getFecFin(), changeDTO.getFecIniPension(), changeDTO.getFecAltaIncapacidad())
                ))
                .concat(NumberUtils.safeValidateInteger(changeDTO.getCveTipoRiesgo()).toString())
                .concat(StringUtils.safeAddZero(changeDTO.getPorcentajeIncapacidad(), 3))
                .concat(StringUtils.concatFullNameFile(changeDTO.getNomAsegurado(), changeDTO.getRefPrimerApellido(), changeDTO.getRefSegundoApellido()))
                .concat(StringUtils.safeAddZero(0, 46));
    }

}
