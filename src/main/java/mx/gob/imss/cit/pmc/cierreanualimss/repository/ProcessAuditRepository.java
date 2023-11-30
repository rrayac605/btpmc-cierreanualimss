package mx.gob.imss.cit.pmc.cierreanualimss.repository;

public interface ProcessAuditRepository {

    void createIncorrect(String action, Long key);

    void createCorrect(String action, Long key);

}
