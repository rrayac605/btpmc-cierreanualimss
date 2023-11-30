package mx.gob.imss.cit.pmc.cierreanualimss.enums;

import lombok.Getter;

@Getter
public enum AuditActionEnum {

    OBTENER_INFO("Obtener información RFC IMSS"),
    RESPALDAR_INFO("Respaldar información cierre anual casuística RFC IMSS"),
    ALMACENAR_ARCHIVOS("Almacenar archivos cierre anual casuística RFC IMSS"),
    DEPOSITAR_ARCHIVOS("Depositar archivos cierre anual casuística RFC IMSS"),
    GENERAR_ARCHIVOS("Generar archivos cierre anual casuística RFC IMSS"),
	
    
    OBTENER_INFO_DESCARTADA("Obtener información descartada RFC IMSS"),
    RESPALDAR_INFO_DESCARTADA("Respaldar información descartada RFC IMSS"),
    ALMACENAR_ARCHIVO("Almacenar archivo RFC IMSS");

    private String desc;

    AuditActionEnum(String desc) {
        this.desc = desc;
    }

}
