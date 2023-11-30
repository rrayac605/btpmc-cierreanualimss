package mx.gob.imss.cit.pmc.cierreanualimss.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditoriaDTO implements Serializable{
	private static final long serialVersionUID = 3410777889867523726L;
	
	private String nomUsuario;
	private String desObservacionesSol;
	private Integer cveIdAccionRegistro;
	private String desAccionRegistro;
	private String desCambio;
	private Date fecAlta;
	private Date fecBaja;
	
	private Integer cveSituacionRegistro;
	private String desSituacionRegistro;
	
}
