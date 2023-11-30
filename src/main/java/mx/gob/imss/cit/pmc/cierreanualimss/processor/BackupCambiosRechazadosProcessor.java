package mx.gob.imss.cit.pmc.cierreanualimss.processor;

import java.util.Date;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.AuditoriaDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.ChangeDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.RespaldoRiesgosDescartadosImssDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.NumberUtils;
import mx.gob.imss.cit.pmc.cierreanualimss.utils.StringUtils;

@Component
@StepScope
public class BackupCambiosRechazadosProcessor implements ItemProcessor<ChangeDTO, RespaldoRiesgosDescartadosImssDTO> {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public RespaldoRiesgosDescartadosImssDTO process(ChangeDTO changeDTO) throws Exception {
        Long key = stepExecution.getJobParameters().getLong(CierreAnualImssConstants.KEY_PARAM_DEL);
        List<AuditoriaDTO> auditoriasList = changeDTO.getAuditorias();
        AuditoriaDTO auditoria = new AuditoriaDTO();
        if (!CollectionUtils.isEmpty(auditoriasList)) {
        	auditoria = auditoriasList.get(auditoriasList.size() - 1 );
		}
        
        RespaldoRiesgosDescartadosImssDTO respaldoCierreAnualImssDTO = new RespaldoRiesgosDescartadosImssDTO();
        respaldoCierreAnualImssDTO.setObjectIdOrigen(changeDTO.getObjectIdCambio());
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
        
        if (auditoria != null) {
        	respaldoCierreAnualImssDTO.setClaveAccionRiesgo(auditoria.getCveIdAccionRegistro());
            respaldoCierreAnualImssDTO.setDescripcionAccionRiesgo(auditoria.getDesAccionRegistro());
		}
        
        
        respaldoCierreAnualImssDTO.setFecRespaldo(new Date());
        return respaldoCierreAnualImssDTO;
    }

}
