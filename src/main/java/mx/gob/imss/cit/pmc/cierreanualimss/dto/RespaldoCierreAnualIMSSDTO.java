package mx.gob.imss.cit.pmc.cierreanualimss.dto;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document("MCB_RESPALDO_CIERRE_ANUAL_CASUISTICA_IMSS")
public class RespaldoCierreAnualIMSSDTO {

    @Id
    private ObjectId oid;
    
    private ObjectId objectIdOrigen;

    private String nss;
    
    private Integer ooadNSS;
    
    private Integer subDelegacionNSS;
    
    private Integer umfNSS;

    private String registroPatronal;

    private Integer ooadRegistroPatronal;
    
    private Integer subDelRegistroPatronal;

    private Integer claveConsecuencia;
    
    private String descripcionConsecuencia;

    private Date fechaInicioAccidente;

    private Integer diasSubsidiados;

    private Date fechaFinTermino;

    private Integer tipoRiesgo;

    private Integer porcentajeIncapacidad;

    private String nombreAsegurado;

    private String curpAsegurado;

    private Integer anioEjecucionRespaldo;

    private Integer claveEstadoRiesgo;

    private String descripcionEstadoRiesgo;

    private Integer claveSituacionRiesgo;

    private String descripcionSituacionRiesgo;

    private String origen;

    private Integer anioRevision;

    private Integer anioCicloCaso;

    private String regPatronal;

    private Integer cveConsecuencia;

    private Date fecInicio;

    private Date fecFin;

    private String fullName;

    private String curp;

    private Integer backupYear;

    private Long key;

    private Date fecRespaldo;
    
    private String razonSocial;


}
