package mx.gob.imss.cit.pmc.cierreanualimss.services;

import java.util.Map;
import java.util.Set;

public interface CierreAnualIMSSService {

    Map<String, Set<Integer>> getFirstJobFailedParams(boolean isReprocess);

    Set<Long> getFirstJobFailedKeyList();

    boolean processIsFinished();

	Map<String, Set<Integer>> obtenerCorrectos(boolean isReprocess);

}
