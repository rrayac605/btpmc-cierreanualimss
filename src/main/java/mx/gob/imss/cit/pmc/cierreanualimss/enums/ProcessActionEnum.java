package mx.gob.imss.cit.pmc.cierreanualimss.enums;

import lombok.Getter;

@Getter
public enum ProcessActionEnum {

    GET_INFO("Obtener información RFC IMSS"),
    BACKUP_INFO("Respaldar información RFC IMSS"),
    GENERATE_FILES("Generar archivos RFC IMSS"),
    FILES_STORAGE("Almacenar archivos RFC IMSS"),
    CIERRE_ANUAL_IMSS_PROCESS("Proceso de cierre anual casuística RFC IMSS"),  // Cuando el proceso finaliza
    
    GET_INFO_DESCARTADA("Obtener información descartada RFC IMSS"),
    BACKUP_INFO_DESCARTADA("Respaldar información descartada RFC IMSS"),
    
    FILE_GENERATION("Generación de archivo RFC IMSS"),
    FILE_STORAGE("Almacenar archivo RFC IMSS"),
//    BANK_FILE("Depositar archivo RFC IMSS"),
//    BANK_FILES("Depositar archivos RFC IMSS")
    ;

    private final String desc;

    ProcessActionEnum(String desc) {
        this.desc = desc;
    }

}
