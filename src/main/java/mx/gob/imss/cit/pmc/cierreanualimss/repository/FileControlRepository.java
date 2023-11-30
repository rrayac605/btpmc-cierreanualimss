package mx.gob.imss.cit.pmc.cierreanualimss.repository;

import java.util.List;
import java.util.Set;

import mx.gob.imss.cit.pmc.cierreanualimss.dto.FileControlDTO;

public interface FileControlRepository {

    void createError(String action, Long key);

    void createCorrect(String action, Long key);

    boolean validate();

    boolean validateUpload(String action);

    Set<Long> findKeyListOfGeneratedFiles();

    Set<Long> findKeyListOfStoredFiles();

    List<FileControlDTO> findAllError(List<String> actions);
    
    List<FileControlDTO> encuentraCorrectos(List<String> accion);

}
