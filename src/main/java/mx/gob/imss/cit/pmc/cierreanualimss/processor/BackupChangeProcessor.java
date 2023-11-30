package mx.gob.imss.cit.pmc.cierreanualimss.processor;

import java.util.Date;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.ChangeDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.RespaldoCierreAnualIMSSDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.NumberUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.StringUtils;

@Component
@StepScope
public class BackupChangeProcessor implements ItemProcessor<ChangeDTO, RespaldoCierreAnualIMSSDTO> {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public RespaldoCierreAnualIMSSDTO process(ChangeDTO changeDTO) throws Exception {
        boolean isReprocess = Boolean.TRUE.toString().equals(
                stepExecution.getJobParameters().getString(CierreAnualImssConstants.REPROCESS_FLAG));
        Long key = stepExecution.getJobParameters().getLong(CierreAnualImssConstants.KEY_PARAM_DEL);
        RespaldoCierreAnualIMSSDTO respaldoCierreAnualImssDTO = new RespaldoCierreAnualIMSSDTO();

        respaldoCierreAnualImssDTO.setObjectIdOrigen( changeDTO.getObjectIdCambio());
        respaldoCierreAnualImssDTO.setNss(changeDTO.getNumNss());
        respaldoCierreAnualImssDTO.setOoadNSS(changeDTO.getCveDelegacionNss());
        respaldoCierreAnualImssDTO.setSubDelegacionNSS(changeDTO.getCveSubdelNss());
        respaldoCierreAnualImssDTO.setUmfNSS(changeDTO.getCveUmfAdscripcion());
        respaldoCierreAnualImssDTO.setRegistroPatronal(changeDTO.getRefRegistroPatronal());
        respaldoCierreAnualImssDTO.setOoadRegistroPatronal(changeDTO.getCveDelRegPatronal());
        respaldoCierreAnualImssDTO.setSubDelRegistroPatronal(changeDTO.getCveSubDelRegPatronal());
        respaldoCierreAnualImssDTO.setCveConsecuencia(NumberUtils.processConsequence(changeDTO.getCveConsecuencia(), changeDTO.getNumDiasSubsidiados()));
        respaldoCierreAnualImssDTO.setDescripcionConsecuencia(changeDTO.getDesConsecuencia());
        respaldoCierreAnualImssDTO.setFechaInicioAccidente(DateUtils.orElse(changeDTO.getFecInicio(), changeDTO.getFecAccidente()));
        respaldoCierreAnualImssDTO.setDiasSubsidiados(changeDTO.getNumDiasSubsidiados());
        respaldoCierreAnualImssDTO.setFechaFinTermino(DateUtils.orElse(changeDTO.getFecFin(), changeDTO.getFecIniPension()));
        respaldoCierreAnualImssDTO.setTipoRiesgo(NumberUtils.safeValidateInteger(changeDTO.getCveTipoRiesgo()));
        respaldoCierreAnualImssDTO.setPorcentajeIncapacidad(NumberUtils.safeValidateInteger(changeDTO.getPorcentajeIncapacidad()));
        respaldoCierreAnualImssDTO.setNombreAsegurado(StringUtils.concatFullName(changeDTO.getNomAsegurado(),
              changeDTO.getRefPrimerApellido(), changeDTO.getRefSegundoApellido(), CierreAnualImssConstants.EMPTY_SPACE));
        respaldoCierreAnualImssDTO.setCurpAsegurado(StringUtils.safeValidateCurp(changeDTO.getRefCurp()));
        respaldoCierreAnualImssDTO.setAnioEjecucionRespaldo(Integer.parseInt(DateUtils.getCurrentYear()));
        respaldoCierreAnualImssDTO.setClaveEstadoRiesgo(changeDTO.getCveEstadoRegistro());
        respaldoCierreAnualImssDTO.setDescripcionEstadoRiesgo(changeDTO.getDesEstadoRegistro());
        respaldoCierreAnualImssDTO.setClaveSituacionRiesgo(changeDTO.getCveSituacionRegistro());
        respaldoCierreAnualImssDTO.setDescripcionSituacionRiesgo(changeDTO.getDesSituacionRegistro());
        respaldoCierreAnualImssDTO.setOrigen(changeDTO.getCveOrigenArchivo());
        respaldoCierreAnualImssDTO.setAnioRevision(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
        respaldoCierreAnualImssDTO.setRazonSocial(changeDTO.getDesRazonSocial());
        if (StringUtils.isNotEmpty(changeDTO.getNumCicloAnual())) {
            respaldoCierreAnualImssDTO.setAnioCicloCaso(Integer.parseInt(changeDTO.getNumCicloAnual()));
		}
        respaldoCierreAnualImssDTO.setFecRespaldo(new Date());
        respaldoCierreAnualImssDTO.setKey(key);
        
        return respaldoCierreAnualImssDTO;
    }

}
