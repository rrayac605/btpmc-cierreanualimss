package mx.gob.imss.cit.pmc.cierreanualimss.services.impl;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.ProcessControlDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.ProcessActionEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.FileControlRepository;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.ProcessControlRepository;
import mx.gob.imss.cit.pmc.cierreanualimss.services.CierreAnualIMSSService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CierreAnualIMSSServiceImpl implements CierreAnualIMSSService {

    @Autowired
    private ProcessControlRepository processControlRepository;

    @Autowired
    private FileControlRepository fileControlRepository;

    @Override
    public Map<String, Set<Integer>> getFirstJobFailedParams(boolean isReprocess) {
        List<ProcessControlDTO> processControlList;
        processControlList = getFirstJobFailedProcessList();
        return listToMap(processControlList);
    }

    private Map<String, Set<Integer>> listToMap(List<ProcessControlDTO> processControlList) {
        return processControlList.stream().collect(Collectors.toMap(
                processControl -> CierreAnualImssConstants.ACTION_TO_TEMPLATE_NAME.get(processControl.getAccion()),
                processControl -> Collections.singleton(processControl.getKey().intValue()),
                (existing, replacement) -> {
                    Set<Integer> mergedSet = new HashSet<>();
                    mergedSet.addAll(existing);
                    mergedSet.addAll(replacement);
                    return mergedSet;
                }
        ));
    }

    @Override
    public Set<Long> getFirstJobFailedKeyList() {
        List<ProcessControlDTO> processControlList = getFirstJobFailedProcessList();
        return processControlList.stream().map(ProcessControlDTO::getKey).collect(Collectors.toSet());
    }
    
    @Override
    public Map<String, Set<Integer>> obtenerCorrectos(boolean isReprocess) {
    	List<ProcessControlDTO> controlProcesoList;
    	controlProcesoList = obtenerCorrectosList();
    	return listToMap(controlProcesoList);
    }

    public List<ProcessControlDTO> getFirstJobFailedProcessList() {
        List<ProcessControlDTO> processControlList = processControlRepository.findAllError(Arrays.asList(
                ProcessActionEnum.GET_INFO.getDesc(),
                ProcessActionEnum.BACKUP_INFO.getDesc()));
        List<ProcessControlDTO> fileControlList = fileControlRepository.findAllError(
                Collections.singletonList(ProcessActionEnum.FILE_GENERATION.getDesc()))
                .stream().map(ProcessControlDTO::new).collect(Collectors.toList());
        return Stream.concat(processControlList.stream(), fileControlList.stream()).collect(Collectors.toList());
    }
    
    public List<ProcessControlDTO> obtenerCorrectosList() {
    	List<ProcessControlDTO> archivosCorrectosList = fileControlRepository.encuentraCorrectos(
    			Collections.singletonList(ProcessActionEnum.FILE_GENERATION.getDesc()))
    			.stream().map(ProcessControlDTO::new).collect(Collectors.toList());
    	return archivosCorrectosList;
    }

    @Override
    public boolean processIsFinished() {
        return processControlRepository.validateAction(ProcessActionEnum.CIERRE_ANUAL_IMSS_PROCESS.getDesc());
    }
}
