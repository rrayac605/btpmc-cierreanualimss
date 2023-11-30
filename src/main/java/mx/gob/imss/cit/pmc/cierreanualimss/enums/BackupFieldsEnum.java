package mx.gob.imss.cit.pmc.cierreanualimss.enums;

import lombok.Getter;

@Getter
public enum BackupFieldsEnum {

    KEY("key"),
    FEC_RESPALDO("fecRespaldo"),
    CVE_CONSECUENCIA("cveConsecuencia"),
    CVE_EDO_REG("claveEstadoRiesgo");

    private final String desc;

    BackupFieldsEnum(String desc) {
        this.desc = desc;
    }

}
