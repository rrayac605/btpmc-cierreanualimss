package mx.gob.imss.cit.pmc.cierreanualimss.repository;

import java.util.List;

import mx.gob.imss.cit.pmc.cierreanualimss.dto.ProcessControlDTO;

public interface ProcessControlRepository {

    void createError(String action, Long key);

    void createCorrect(String action, Long key);

    boolean validateAction(String action);

    List<ProcessControlDTO> findAllError(List<String> actions);

}
