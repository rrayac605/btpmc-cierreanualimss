package mx.gob.imss.cit.pmc.cierreanualimss.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;

public class NumberUtils {

    public static Integer safeValidateInteger(Integer integer) {
        return isNotNullInteger(integer) ? integer : CierreAnualImssConstants.ZERO;
    }

    public static Long safeValidateLong(Long num) {
        return isNotNullLong(num) ? num : CierreAnualImssConstants.ZERO;
    }

    public static Integer safetyParseBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal != null ? bigDecimal.intValue() : CierreAnualImssConstants.ZERO;
    }

    public static Boolean isNullInteger(Integer integer) { return integer == null; }

    public static Boolean isNullLong(Long num) { return num == null; }

    public static Boolean isNotNullInteger(Integer integer) { return !isNullInteger(integer); }

    public static Boolean isNotNullLong(Long num) { return !isNullLong(num); }

    public static Integer processConsequence(Integer consequence) {
        if (consequence != null) {
            return consequence == 0 || consequence > 9
                    ? CierreAnualImssConstants.CONSEQUENCE_EQUIVALENCE.get(consequence)
                    : consequence;
        } else {
            return CierreAnualImssConstants.FIRST;
        }
    }

    public static Integer processConsequence(Integer consequence, Integer subsidizedDays) {
        if (consequence != null) {
            return consequence == 0 || consequence > 9
                    ? CierreAnualImssConstants.CONSEQUENCE_EQUIVALENCE.get(consequence): consequence;
        } else {
            return subsidizedDays != null && subsidizedDays > 0 ? 1 : 0;
        }
    }
    
    public static List<Integer> getSubDelList(Long key) {
        List<Integer> delSubDel = CierreAnualImssConstants.DEL_SUBDEL.get(key.intValue());
        return delSubDel.subList(1, delSubDel.size());
    }

    public static Integer getDel(Long key) {
        return CierreAnualImssConstants.DEL_SUBDEL.get(key.intValue()).get(CierreAnualImssConstants.ZERO);
    }
    
    public static Integer getOrdenEjecucion() {   
    	Integer ordenEjecucion = 9999;  //Si se ejecuta en otro momento, la consideramos ejecucion especial no definido en requerimiento
    	int mes = LocalDate.now().getMonthValue();
    	
    	if (mes == 6 || mes == 7) {
    		ordenEjecucion = 1;
		} else if(mes == 1) {
			ordenEjecucion = 2;
		} else if(mes == 2) {
			ordenEjecucion = 3;
		} 
        return ordenEjecucion;
    }
    
    public static void main(String[] args) {
    	
    	
    	System.out.println(DateUtils.getStartProcessDateString());
    	System.out.println(DateUtils.getEndProcessDateString());
    	
    	System.out.println(DateUtils.getCalendarMonth());
    	
    	System.out.println(LocalDate.now().getMonth());
    	System.out.println(NumberUtils.getOrdenEjecucion());
    	
    	}

}
