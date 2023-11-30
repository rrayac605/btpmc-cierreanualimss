package mx.gob.imss.cit.pmc.cierreanualimss.repository;

import mx.gob.imss.cit.pmc.cierreanualimss.dto.EmailTemplateDTO;

public interface EmailTemplateRepository {

    EmailTemplateDTO findByName(String name);

}
