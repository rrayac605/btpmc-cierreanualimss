package mx.gob.imss.cit.pmc.cierreanualimss.repository;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.cierreanualimss.dto.ParameterDTO;

public interface ParameterRepository {

    Optional<ParameterDTO<String>> findOneByCve(String cve);

    Optional<ParameterDTO<List<String>>> findListByCve(String cve);

}
