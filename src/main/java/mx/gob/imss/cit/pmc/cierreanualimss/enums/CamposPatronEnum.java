package mx.gob.imss.cit.pmc.cierreanualimss.enums;

import lombok.Getter;

@Getter
public enum CamposPatronEnum {

    REGISTRO_PATRONAL("patronDTO.refRegistroPatronal"),
    CVE_DEL_PATRON("patronDTO.cveDelRegPatronal"),
    CVE_SUBDEL_PATRON("patronDTO.cveSubDelRegPatronal"),
	DES_RFC("patronDTO.desRfc");

    private final String desc;

    CamposPatronEnum(String desc) {
        this.desc = desc;
    }

}
