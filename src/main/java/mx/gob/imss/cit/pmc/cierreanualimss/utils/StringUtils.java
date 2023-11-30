package mx.gob.imss.cit.pmc.cierreanualimss.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.ParameterDTO;

public class StringUtils {

	public static String safeValidate(String str) {
		return isNotEmpty(str) ? str : CierreAnualImssConstants.EMPTY;
	}

	public static String safeValidateCurp(String curp) {
		return isNotEmpty(curp) ? curp : CierreAnualImssConstants.MISSING_CURP;
	}

	public static Boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	public static Boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String safeSubString(String str, Integer length) {
		if (isNotEmpty(str)) {
			return str.length() <= length ? str : str.substring(CierreAnualImssConstants.ZERO, length);
		}
		return CierreAnualImssConstants.MISSING_NSS_RP;
	}

	public static String safeAddZero(Integer num, Integer numZeros) {
		return String.format("%0" + numZeros + "d", NumberUtils.safeValidateInteger(num));
	}

	public static String concatFullNameFile(String nombre, String apellidoP, String apellidoM) {
		String fullName = concatFullName(nombre, apellidoP, apellidoM, "$");
		return String.format("%-50s", fullName);
	}

	public static String concatFullName(String nombre, String apellidoP, String apellidoM, String separator) {
		return safeValidate(apellidoP).trim().concat(separator).concat(safeValidate(apellidoM).trim()).concat(separator)
				.concat(safeValidate(nombre).trim());
	}

	public static String injectYear(String str) {
		return injectParam(DateUtils.getCurrentYear(), str, "{year}");
	}

	public static String injectMonth(String str) {
		return injectParam(DateUtils.getCalendarMonth(), str, "{month}");
	}

	public static String injectParam(String param, String str, String placeholder) {
		return str.replace(placeholder, param);
	}

	public static String buildFileName(Long del, String basePath) {
		return buildFilePath(basePath).concat(nombreArchivo(del));
	}

	public static String buildFilePath(String basePath) {
		String year = StringUtils.injectYear(basePath);
		return StringUtils.injectMonth(year);
	}

	public static String getFromParam(Optional<ParameterDTO<String>> parameterDTO) {
		return parameterDTO.map(ParameterDTO::getDesParametro).orElse(CierreAnualImssConstants.EMPTY);
	}

	public static String nombreArchivo(Long del) {
		List<Integer> delSubDel = CierreAnualImssConstants.DEL_SUBDEL_NOMBRE.get(del.intValue());
		StringBuilder subDel = new StringBuilder();
		for (Integer x : delSubDel) {
			subDel.append("_").append(String.format("%02d",x));
		}
			subDel.append(CierreAnualImssConstants.FILE_EXTENSION);
			//En este punto concatena las claves de delegacion y subdelegaciones para el nombre
		return CierreAnualImssConstants.FILE_NAME.concat(subDel.toString()); 
	}
	
	//RN146
	public static String getDelegacionFiller(Integer del, String subDel) {
        List<Integer> subDelList = StringUtils.subDelToList(subDel);

		Integer delToWritte;
		if (del.equals(8) && subDelList.contains(10)) {
			delToWritte = 10;
		} else if (del.equals(27) && subDelList.contains(1)) {
			delToWritte = 1;
		} else if (del.equals(29) && subDelList.contains(19)) {
			delToWritte = 19;
		} else if (del.equals(26) && subDelList.contains(5)) {
			delToWritte = 5;
		} else if (del.equals(2) && subDelList.contains(5)) {
			delToWritte = 5;
		} else if (del.equals(5) && subDelList.contains(9)) {
			delToWritte = 9;
		} else if (del.equals(31) && subDelList.contains(12)) {
			delToWritte = 12;
		} else {
			delToWritte = del;
		}
		
		return String.format("%02d", delToWritte);
	}
	
	public static String subDelToString(List<Integer> subDelList) {
		StringBuilder subDel = new StringBuilder(); 
		for (int i = 0; i < subDelList.size(); i++) {
			subDel.append(subDelList.get(i));
			
			if (i < (subDelList.size() - 1) ) {
				subDel.append(",");
			}
			
		}
		return subDel.toString();
	}
	
	public static List<Integer> subDelToList(String subDelString) {
		String[] listSubDel = subDelString.split(",");
		List<Integer> subDels = new ArrayList<>();
		for (String string : listSubDel) {
			subDels.add(Integer.valueOf(string));
		}
		return subDels;
	}

}
