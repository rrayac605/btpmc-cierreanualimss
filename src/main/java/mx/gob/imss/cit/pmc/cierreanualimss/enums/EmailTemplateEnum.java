package mx.gob.imss.cit.pmc.cierreanualimss.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateEnum {

    PROCESS_SUCCESSFUL("CIERRE_ANUAL_IMSS_PROCESS_SUCCESSFUL"),
    GET_INFO_FAILED("CIERRE_IMSS_ERROR_OBTENER_INFO"),
    FILE_GENERATION_FAILED("CIERRE_IMSS_ERROR_GENERAR_ARCHIVO"),
    BACKUP_DATA_FAILED("CIERRE_IMSS_ERROR_RESPALDO_INFO"),
    UPLOAD_FILES_FAILED("CIERRE_IMSS_ERROR_ALMACENAR_ARCHIVO");

    private final String desc;

    EmailTemplateEnum(String desc) {
        this.desc = desc;
    }
}
