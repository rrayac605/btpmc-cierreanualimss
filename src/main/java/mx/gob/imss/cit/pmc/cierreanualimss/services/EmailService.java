package mx.gob.imss.cit.pmc.cierreanualimss.services;

import java.util.Set;

public interface EmailService {

    void sendEmail(String templateName, Set<Long> keyList);

    void sendEmailGetBackupInfoFailedList(String templateName, Set<Long> keyList);
    
    void sendEmailToOOADList(String templateName, Set<Long> keyList, Set<Long> keyCorrectos);
    
    void sendEmailPasoUno(String templateName);
    
    void sendEmailPasoDosTresCuatro(String templateName);

}
