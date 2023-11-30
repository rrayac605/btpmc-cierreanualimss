package mx.gob.imss.cit.pmc.cierreanualimss.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;

public class DateUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static String getFileFormattedDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.PATTERN_YYYYMMDD);
        df.setTimeZone(tz);
        return df.format(date);
    }
    
    public static String getFileFormattedDateChanges(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.PATTERN_YYYYMMDD);
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static String getFileFormattedHour(Date date) {
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.PATTERN_TIME);
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static String getCurrentMexicoDateString() {
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.PATTERN_DDMMYYYY_TIME);
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public static String getCurrentMongoDateString(boolean isReprocess) {
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.PATTERN_YYYY_MM_DD_TIME);

        df.setTimeZone(tz);
        Date currentDate = new Date();
        if (isReprocess) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            currentDate = calendar.getTime();
        }
        return df.format(currentDate).concat("T23:00:00Z");
    }
    
    public static String obtenerMongoDateString() {
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.PATTERN_YYYY_MM_DD_TIME);

        df.setTimeZone(tz);
        Date currentDate = new Date();
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        currentDate = calendar.getTime();
       
        return df.format(currentDate).concat("T23:00:00Z");
    }
    
    public static String obtenerDiaEjecucion(Date date) {    	
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.FORMATO_DIA_EJECUCION);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
    	calendar.setTimeZone(tz);
    	
    	int horaDelDia = calendar.get(Calendar.HOUR_OF_DAY);
    	
    	logger.info("Valor de la hora {}", horaDelDia);
        
        if(horaDelDia >= 0) {
        	logger.info("Ya paso el dia de ejecucion");
        	calendar.add(Calendar.DAY_OF_YEAR, -1);
        	Date dt = calendar.getTime();
        	dt = calendar.getTime();
        	
        	return df.format(dt);
        } else {
        	logger.info("Aun no pasa el dia de ejecucion");
        	df.setTimeZone(tz);
        	return df.format(new Date());
        }    
    }
    
    public static String obtenerHoraEjec() {
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.FORMATO_HORA_EJEC);
        df.setTimeZone(tz);
        return df.format(new Date());
    }
    
    public static String obtenerDiaSig(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.FORMATO_DIA_EJECUCION);
        
        calendar.setTime(date);
    	calendar.setTimeZone(tz);
    	
    	int horaDelDia = calendar.get(Calendar.HOUR_OF_DAY);
    	
    	logger.info("Valor de la hora {}", horaDelDia);
        
        if(horaDelDia >= 0) {
        	logger.info("Ya estoy en el dia siguiente");
        	calendar.get(Calendar.DAY_OF_YEAR);
        	Date dt = calendar.getTime();
        	dt = calendar.getTime();
        	
        	return df.format(dt);
        } else {
        	logger.info("Aun no llego al dia siguiente pero le voy a sumar 1");
        	calendar.add(Calendar.DAY_OF_YEAR, +1);
        	Date dt = calendar.getTime();
        	dt = calendar.getTime();
        	
        	return df.format(dt);
        }
    }
    
    public static String obtenerDiaFin() {
    	TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.FORMATO_DIA_EJECUCION);
        
        df.setTimeZone(tz);
        
        return df.format(new Date());
    }
    
    public static String obtenerHoraFin() {
    	TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.FORMATO_HORA_EJEC);
        df.setTimeZone(tz);
        return df.format(new Date());
    }
    
    public static String getStartProcessDateString() {
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.PATTERN_YYYY_MM_DD_TIME);
        
        ZoneId zoneId = ZoneId.of( "America/Mexico_City" );  // Or 'ZoneOffset.UTC'.
        ZonedDateTime now = ZonedDateTime.now( zoneId );
        Month month = now.getMonth(); 

        Calendar hoy = Calendar.getInstance();
        hoy.setTime(new Date());
        
        //RN155 junio anio - 1
        if (month.getValue() == 1 || month.getValue() == 2) { // anio -2
			hoy.add(Calendar.YEAR, -1); //TODO: debe ser -1  , se modifico la regla
		}
        
        hoy.set(Calendar.DAY_OF_MONTH, 0);
        hoy.set(Calendar.MONTH, 1);
        
        return df.format(hoy.getTime()).concat("T00:00:00Z");
    }
    
    public static String getEndProcessDateString() {
      DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.PATTERN_YYYY_MM_DD_TIME);
      
      ZoneId zoneId = ZoneId.of( "America/Mexico_City" );  // Or 'ZoneOffset.UTC'.
      ZonedDateTime now = ZonedDateTime.now( zoneId );
      Month month = now.getMonth(); 

      Calendar hoy = Calendar.getInstance();
      hoy.setTime(new Date());
      
      //RN155 enero y febrero anio - 1
      if (month.getValue() == 1 || month.getValue() == 2) { // anio -1
			hoy.add(Calendar.YEAR, -1);
		} 
      hoy.set(Calendar.DAY_OF_MONTH, 15);
      hoy.set(Calendar.MONTH, 6);// DEBE SER MES 2 - MARZO 
      
      return df.format(hoy.getTime()).concat("T23:59:00Z");
  }

    public static Date getCurrentMexicoDate() {
        try {
            return new SimpleDateFormat(CierreAnualImssConstants.PATTERN_DDMMYYYY_TIME).parse(getCurrentMexicoDateString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentYear() {
        String actualDate = getCurrentMexicoDateString();
        return actualDate.substring(4, 8);
    }

    public static String getCurrentYY() {
    	String startDate = DateUtils.getStartProcessDateString();
        return startDate.substring(2, 4);
    }

    private static String getFormattedDay(String day, String lastDayOfMonth) {
        return day != null && !day.equals("null") ? Integer.parseInt(day) < 10 ? CierreAnualImssConstants.ZERO_STRING.concat(day) : day
                : lastDayOfMonth != null ? lastDayOfMonth : CierreAnualImssConstants.FIRST_DAY;
    }

    private static String getFormattedMonth(String month) {
        return month.length() < 2 ? CierreAnualImssConstants.ZERO_STRING.concat(month) : month;
    }

    /**
     * Utileria que genera la fecha con las horas adecuadas para realizar la
     * consulta de movimientos, se agregan 6 horas a esta fecha dado que al momento
     * de realizar la busqueda mongodb agrega 6 horas y con estas se compenza la
     * busqueda de la misma
     */
    public static Date calculateBeginDate(String year, String month, String day) {
        String stringDate = getFormattedDay(day, null).concat(getFormattedMonth(month)).concat(year)
                .concat(CierreAnualImssConstants.BEGIN_HOURS);
        TimeZone tz = TimeZone.getTimeZone(CierreAnualImssConstants.ISO_TIMEZONE);
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.PATTERN_DDMMYYYY_TIME);
        df.setTimeZone(tz);
        try {
            Date formattedDate = df.parse(stringDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formattedDate);
            calendar.add(Calendar.HOUR, CierreAnualImssConstants.HOURS_TO_ADD);
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Utileria que genera la fecha con las horas adecuadas para realizar la
     * consulta de movimientos, se agregan 6 horas a esta fecha dado que al momento
     * de realizar la busqueda mongodb agrega 6 horas y con estas se compenza la
     * busqueda de la misma
     */
    public static Date calculateEndDate(String year, String month, String day) {
        LocalDateTime initial = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), CierreAnualImssConstants.TEN,
                CierreAnualImssConstants.FIRST, CierreAnualImssConstants.ZERO, CierreAnualImssConstants.ZERO, CierreAnualImssConstants.ZERO);
        initial = initial.with(TemporalAdjusters.lastDayOfMonth());
        String stringDate = getFormattedDay(day, String.valueOf(initial.getDayOfMonth()))
                .concat(getFormattedMonth(month)).concat(year).concat(CierreAnualImssConstants.END_HOURS);
        TimeZone tz = TimeZone.getTimeZone(CierreAnualImssConstants.ISO_TIMEZONE);
        DateFormat df = new SimpleDateFormat(CierreAnualImssConstants.PATTERN_DDMMYYYY_TIME);
        df.setTimeZone(tz);
        try {
            Date formattedDate = df.parse(stringDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formattedDate);
            calendar.add(Calendar.HOUR, CierreAnualImssConstants.HOURS_TO_ADD);
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date orElse(Date principal, Date secondary) {
        return principal != null ? principal : secondary;
    }

    public static Date orElse(Date principal, Date secondary, Date third) {
        return principal != null ? principal : secondary != null ? secondary : third;
    }

    public static int getCurrentJulianDay() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(Objects.requireNonNull(getCurrentMexicoDate()));
        return calendar.get(GregorianCalendar.DAY_OF_YEAR);
    }

    public static String getAACCC() {
        String AA = getCurrentYY();
        String CCC = "001";
        return AA.concat(CCC);
    }
    
    
    public static String getCalendarMonth() {   
        ZoneId zoneId = ZoneId.of( "America/Mexico_City" );  // Or 'ZoneOffset.UTC'.
        ZonedDateTime now = ZonedDateTime.now( zoneId );
        Month month = now.getMonth();

        String mes = month.getDisplayName( TextStyle.FULL , new Locale("es", "ES") );

        if(mes.equals(CierreAnualImssConstants.MES_SEPTIEMBRE)) {
            month = Month.of(month.getValue() - 1);
        }

        return month.getDisplayName( TextStyle.FULL , new Locale("es", "ES") );
    }
    
    
    
    public static void main(String[] args) {
    	
    	
    	System.out.println(DateUtils.getStartProcessDateString());
    	System.out.println(DateUtils.getEndProcessDateString());
    	
    	System.out.println(DateUtils.getCalendarMonth());
    	
    	System.out.println(LocalDate.now().getMonth());
    	System.out.println(LocalDate.now().getMonthValue());
    	
    	}
    } 


