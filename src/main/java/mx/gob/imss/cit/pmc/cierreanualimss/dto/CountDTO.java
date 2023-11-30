package mx.gob.imss.cit.pmc.cierreanualimss.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountDTO {
	
	private List<?> totales;

    private Object defunciones;
    
    private Object porcentaje;
    
    private Object diasSubsidiados;
    
    private Long count;
    
    private String del;
    
    private String nombreDel;
    
    private Long totalCorrectos;
    
    private Long totalCorrectosOtrasDel;
    
    private Long totalSuceptiblesAjuste;
    
    private Long totalSuceptiblesAjusteOtrasDel;
    
    private Long totalDefunciones;
    
    private Long totalPorcentajeIncapacidad;
    
    private Long totaldiasSubsidiados;

}
