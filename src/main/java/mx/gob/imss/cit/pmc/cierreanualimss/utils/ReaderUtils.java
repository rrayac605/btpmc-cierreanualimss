package mx.gob.imss.cit.pmc.cierreanualimss.utils;

import java.util.List;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import mx.gob.imss.cit.pmc.cierreanualimss.constants.CierreAnualImssConstants;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.ChangeDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.MovementDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.dto.RespaldoCierreAnualIMSSDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.BackupFieldsEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.CamposAseguradoEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.CamposIncapacidadEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.CamposPatronEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.MovementFieldsEnum;

public class ReaderUtils {
	
    static String startDate = DateUtils.getStartProcessDateString();
    static String endDate = DateUtils.getEndProcessDateString();
    static String revisionYear = startDate.substring(0, 4);

    
    public static String buildMovimientosJSONQuery(Integer del, List<Integer> subDelList) {

    	return "{ '$and' : [" +
        		" { 'patronDTO.desRfc' : 'IMS421231I45' },"	+
                " { 'aseguradoDTO.fecBaja' : null}," +
                " { 'aseguradoDTO.cveEstadoRegistro' : { '$in' : [1, 5, 4, 8]}}," +
                " { '$or' : [{ 'isPending' : null }, { 'isPending' : false}]}," +
                " { 'aseguradoDTO.numCicloAnual': '" + Integer.valueOf(revisionYear) +"' }," +
                " { 'incapacidadDTO.cveConsecuencia' : { '$in' : [null, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]}}," +
                " { 'patronDTO.cveDelRegPatronal' : " + NumberUtils.getDel(Long.valueOf(del)) + "}," +
                " { 'patronDTO.cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + "}}" +
                "]}";
    }

    public static String buildCambiosJSONQuery(Integer del, List<Integer> subDelList) {
    	return "{'$and' : [" +
        		"    { 'desRfc' : 'IMS421231I45' }," + 
                "    { 'cveOrigenArchivo' : 'MN'}," +
                "    { 'fecBaja' : null}," +
                "    { 'cveEstadoRegistro' : { '$in' : [1, 5, 4, 8]}}," +
                "    { '$or' : [{ 'isPending' : null}, { 'isPending' : false}]}," +
                "    { 'cveSituacionRegistro' : 1}," +
                "    { 'cveConsecuencia' : { '$in' : [null, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]}}," +
                "    { 'numCicloAnual': '" + Integer.valueOf(revisionYear) + "' }," +
                "    { 'cveDelRegPatronal' : " + NumberUtils.getDel(Long.valueOf(del)) +" }," +
                "    { 'cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + "}}" +
                "]}";
    }
    
/************************************ DESCARTADOS ****************************************/    
    
    public static String buildMovimientosDescartadosJSONQuery(Integer del, List<Integer> subDelList) {
    	return  " {" +
        		" 'patronDTO.desRfc' : 'IMS421231I45' ,"	+
                " 'aseguradoDTO.numCicloAnual': '" + Integer.valueOf(revisionYear) +"' ," +
                " 'patronDTO.cveDelRegPatronal' : " + NumberUtils.getDel(Long.valueOf(del)) + "," +
                " 'patronDTO.cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + "}," +
                "    '$or' : ["
				+ "        { "
				+ "            '$and' : ["
				+ "                { 'aseguradoDTO.cveEstadoRegistro' : { '$in' : [2, 6, 3, 7] } },"
				+ "                { '$or' : ["
				+ "                    { 'isPending' : true },"
				+ "                    { 'isPending' : { '$exists' : false  } }"
				+ ""
				+ "                ] }"
				+ "            ] "
				+ "        },"
				+ "        {"
				+ "            '$and' : ["
				+ "                { 'aseguradoDTO.cveEstadoRegistro' : { '$in' : [10, 11] } }"
				+ "            ]"
				+ "        }"
				+ "    ]"
                + "}";
    }
    
    public static String buildMovimientosDescartadosAnterioresJSONQuery(Integer del, List<Integer> subDelList) {
        return  "{  'patronDTO.desRfc' : 'IMS421231I45'," + 
        		"   'aseguradoDTO.numCicloAnual' : { '$lt' : '" + Integer.valueOf(revisionYear) + "' }," + 
				"   'patronDTO.cveDelRegPatronal' : " + NumberUtils.getDel(Long.valueOf(del)) + "," +
                "   'patronDTO.cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + "}," +
				"   '$or':[ "
				+ "          { "
				+ "             \"isPending\":false "
				+ "          }, "
				+ "          { "
				+ "             \"isPending\":{ "
				+ "                \"$exists\":false "
				+ "             } "
				+ "          } "
				+ "       ]" 
				+ "}";
    }
    
    public static String buildMovimientosDescartadosPosterioresJSONQuery(Integer del, List<Integer> subDelList) {
    	return  "{"+ 
        		"   'patronDTO.desRfc' : 'IMS421231I45'," + 
        		"   'aseguradoDTO.numCicloAnual' : { '$gt' : '" + Integer.valueOf(revisionYear) + "' }," +
				"   'patronDTO.cveDelRegPatronal' : " + NumberUtils.getDel(Long.valueOf(del)) + "," +
                "   'patronDTO.cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + "}," +
				"	 \"$or\":[ "
				+ "          { "
				+ "             \"isPending\":false "
				+ "          }, "
				+ "          { "
				+ "             \"isPending\":{ "
				+ "                \"$exists\":false "
				+ "             } "
				+ "          } "
				+ "       ]" 
				+ "}";
    }

    public static String buildCambiosDescartadosJSONQuery(Integer del, List<Integer> subDelList) {
    	return  " {" +
        		"     'desRfc' : 'IMS421231I45' ," +
                "     'cveDelRegPatronal' : " + NumberUtils.getDel(Long.valueOf(del)) +" ," +
                "     'cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + "}," +
                "     'numCicloAnual': '" + Integer.valueOf(revisionYear) + "' ," +
				"'$or' : ["
        		+ "        { '$and' : ["
        		+ "           { cveEstadoRegistro : { '$in' : [10, 11] } },"
        		+ "           { '$or' : ["
        		+ "            { cveSituacionRegistro : 2 }"
        		+ "           ] }"        	
        		+ "        ] },"
        		+ "        { '$and' : ["
        		+ "           { idOrigenAlta : { '$exists' : false}}, "
        		+ "           { cveOrigenArchivo: 'MN'}, "
        		+ "           { cveSituacionRegistro : 1 },"
        		+ "           { cveEstadoRegistro : { '$in' : [10, 11] } }"        	
        		+ "        ] },"
        		+ "        { '$and' : ["
        		+ "            { cveEstadoRegistro : { '$in' : [4, 8] } },"
        		+ "            { cveSituacionRegistro : 2 }"
        		+ "        ] },"
        		+ "        { '$and' : ["
        		+ "            { cveEstadoRegistro : { '$in' : [1, 5, 4, 8] } },"
        		+ "            { cveOrigenArchivo : 'MN' },"
        		+ "            { cveSituacionRegistro : 3 },"
        		+ "            {"
        		+ "                \"idOrigenAlta\":{$exists:false} "
        		+ "            }"
        		+ "        ] },"
        		+ "				 {"
        		+ "         \"$and\":["
        		+ "            {"
        		+ "               \"cveEstadoRegistro\":{"
        		+ "                  \"$in\":["
        		+ "                     1,5,4,8"
        		+ "                  ]"
        		+ "               }"
        		+ "            },"
        		+ "            {"
        		+ "               \"cveSituacionRegistro\":{$in:[ 2]}"
        		+ "            },"
        		+ "            {"
        		+ "               \"fecBaja\":{$exists:false}"
        		+ "            }"
        		+ "         ]}"
        		+ "    ]    "
        		+ "}";
    }

    public static String buildCambiosDescartadosAnterioresJSONQuery(Integer del, List<Integer> subDelList) {
    	return  "   {'desRfc' : 'IMS421231I45'," + 
        		"   'numCicloAnual' : { '$lt' : '" + Integer.valueOf(revisionYear) + "' }," +
				"   'cveDelRegPatronal' : " + NumberUtils.getDel(Long.valueOf(del)) + "," +
                "   'cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + "}," +
				"	    \"$or\":[ "
				+ "      { "
				+ "         \"$and\":[ "
				+ "            { "
				+ "               \"cveOrigenArchivo\":{ "
				+ "                  \"$eq\":\"MN\" "
				+ "               } "
				+ "            }, "
				+ "            { "
				+ "               \"cveEstadoRegistro\":{ "
				+ "                  \"$in\":[ "
				+ "                     10, "
				+ "                     11 "
				+ "                  ] "
				+ "               } "
				+ "            }, "
				+ "            { "
				+ "               \"idOrigenAlta\":{ "
				+ "                  \"$exists\":false "
				+ "               } "
				+ "            } "
				+ "         ] "
				+ "      }, "
				+ "      { "
				+ "         \"$and\":[ "
				+ "            { "
				+ "               \"fecBaja\":{ "
				+ "                  \"$exists\":false "
				+ "               } "
				+ "            } "
				+ "         ] "
				+ "      } "
				+ "   ]" 
				+ " }";
    }
    
    public static String buildCambiosDescartadosPosterioresJSONQuery(Integer del, List<Integer> subDelList) {
    	return  "   {'desRfc' : 'IMS421231I45'," + 
        		"   'numCicloAnual' : { '$gt' : '" + Integer.valueOf(revisionYear) + "' }," +
				"   'cveDelRegPatronal' : " + NumberUtils.getDel(Long.valueOf(del)) + "," +
                "   'cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + "}," +
				"	    \"$or\":[ "
		+ "      { "
		+ "         \"$and\":[ "
		+ "            { "
		+ "               \"cveOrigenArchivo\":{ "
		+ "                  \"$eq\":\"MN\" "
		+ "               } "
		+ "            }, "
		+ "            { "
		+ "               \"cveEstadoRegistro\":{ "
		+ "                  \"$in\":[ "
		+ "                     10, "
		+ "                     11 "
		+ "                  ] "
		+ "               } "
		+ "            }, "
		+ "            { "
		+ "               \"idOrigenAlta\":{ "
		+ "                  \"$exists\":false "
		+ "               } "
		+ "            } "
		+ "         ] "
		+ "      }, "
		+ "      { "
		+ "         \"$and\":[ "
		+ "            { "
		+ "               \"fecBaja\":{ "
		+ "                  \"$exists\":false "
		+ "               } "
		+ "            } "
		+ "         ] "
		+ "      } "
		+ "   ]" 
		+ " }";
    }
    
    
    /************************************ DESCARTADOS FIN ****************************************/    

    
    public static TypedAggregation<MovementDTO> buildMovementsCountAggregation(Integer del, List<Integer> subDelList, Integer[] estadoReg, Integer[] concecuencia, Integer op) {
    	String revisionYear = startDate.substring(0, 4);
    	Criteria criteriaRFC = Criteria.where(CamposPatronEnum.DES_RFC.getDesc()).is(CierreAnualImssConstants.RFC_IMSS);
    	MatchOperation matchRFC = new MatchOperation(criteriaRFC);

    	Criteria criteriaFecBaja = Criteria.where(CamposAseguradoEnum.FEC_BAJA.getDesc()).is(null);
    	MatchOperation matchFecBaja = new MatchOperation(criteriaFecBaja);
    	
    	Criteria criteriaEstadoRegistro = Criteria.where(CamposAseguradoEnum.ESTADO_REGISTRO.getDesc()).in(estadoReg);
    	MatchOperation matchEstadoRegistro = new MatchOperation(criteriaEstadoRegistro);

    	Criteria criteriaIsPending = new Criteria().orOperator(
                Criteria.where(MovementFieldsEnum.IS_PENDING.getDesc()).is(null),
                Criteria.where(MovementFieldsEnum.IS_PENDING.getDesc()).is(Boolean.FALSE)
        );
    	MatchOperation matchIsPending = new MatchOperation(criteriaIsPending);

    	Criteria criteriaIsPending2 = Criteria.where(MovementFieldsEnum.IS_PENDING.getDesc()).is(Boolean.FALSE);
    	MatchOperation matchIsPending2 = new MatchOperation(criteriaIsPending2);
    	
    	Criteria criteriaConsecuencia = Criteria.where(CamposIncapacidadEnum.CONSECUENCIA.getDesc()).in(concecuencia);
    	MatchOperation matchConsecuencia = new MatchOperation(criteriaConsecuencia);

    	Criteria criteriaDelPatron = Criteria.where(CamposPatronEnum.CVE_DEL_PATRON.getDesc()).is(NumberUtils.getDel(Long.valueOf(del)));
    	MatchOperation matchDelPatron = new MatchOperation(criteriaDelPatron);
    	
    	Criteria criteriaSubDelPatron  = Criteria.where(CamposPatronEnum.CVE_SUBDEL_PATRON.getDesc()).in(subDelList);
    	MatchOperation matchSubDelPatron = new MatchOperation(criteriaSubDelPatron);
    	
    	Criteria criteriaCicloAnual = Criteria.where(CamposAseguradoEnum.CYCLE.getDesc()).is(revisionYear);
    	MatchOperation matchCicloAnual = new MatchOperation(criteriaCicloAnual);
    	
    	String matchDateJSON = "{$match:{ '" + CamposAseguradoEnum.FEC_ALTA.getDesc() + "': { $gte:ISODate('" + startDate + "'),$lt:ISODate('" + endDate + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
        
        GroupOperation group = null;
        if (op.equals(1)) {
        	group = Aggregation.group().count().as("count");
		} else if (op.equals(2)) {
            group = Aggregation.group().sum("{ $toInt : incapacidadDTO.porPorcentajeIncapacidad }").as("count");
		} else if (op.equals(3)) {
			group = Aggregation.group().sum("incapacidadDTO.numDiasSubsidiados").as("count");
		} 
        
        if (del != null) {
            return Aggregation.newAggregation(MovementDTO.class, matchRFC, matchDate, matchDelPatron, matchSubDelPatron, matchFecBaja,  matchEstadoRegistro, matchIsPending, matchConsecuencia, matchCicloAnual, group);
		} else {
	        return Aggregation.newAggregation(MovementDTO.class, matchRFC, matchDate, matchFecBaja,  matchEstadoRegistro, matchIsPending, matchConsecuencia, matchCicloAnual, group);
		}
    }
    
    
    public static TypedAggregation<ChangeDTO> buildChangesCountAggregation(Integer del, List<Integer> subDelList, Integer[] estadoReg, Integer[] concecuencia, Integer op) {
    	String revisionYear = startDate.substring(0, 4);
    	Criteria criteriaRFC = Criteria.where("desRfc").is(CierreAnualImssConstants.RFC_IMSS);
    	MatchOperation matchRFC = new MatchOperation(criteriaRFC);

    	Criteria criteriaFecBaja = Criteria.where("fecBaja").is(null);
    	MatchOperation matchFecBaja = new MatchOperation(criteriaFecBaja);
    	
    	Criteria criteriaEstadoRegistro = Criteria.where("cveEstadoRegistro").in(estadoReg);
    	MatchOperation matchEstadoRegistro = new MatchOperation(criteriaEstadoRegistro);

    	Criteria criteriaIsPending = Criteria.where(MovementFieldsEnum.IS_PENDING.getDesc()).is(Boolean.FALSE);
    	MatchOperation matchIsPending = new MatchOperation(criteriaIsPending);
    	
    	Criteria criteriaConsecuencia = Criteria.where("cveConsecuencia").in(concecuencia);
    	MatchOperation matchConsecuencia = new MatchOperation(criteriaConsecuencia);

    	Criteria criteriaDelPatron = Criteria.where("cveDelRegPatronal").is(NumberUtils.getDel(Long.valueOf(del)));
    	MatchOperation matchDelPatron = new MatchOperation(criteriaDelPatron);
    	
    	Criteria criteriaSubDelPatron  = Criteria.where("cveSubDelRegPatronal").in(subDelList);
    	MatchOperation matchSubDelPatron = new MatchOperation(criteriaSubDelPatron);
    	
    	Criteria criteriaCicloAnual = Criteria.where("numCicloAnual").is(revisionYear);
    	MatchOperation matchCicloAnual = new MatchOperation(criteriaCicloAnual);
    	
    	String matchDateJSON = "{$match:{ 'fecAlta': { $gte:ISODate('" + startDate + "'),$lt:ISODate('" + endDate + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
        
        GroupOperation group = null;
        if (op.equals(1)) {
        	group = Aggregation.group().count().as("count");
		} else if (op.equals(2)) {
//            group = Aggregation.group().sum("{ '$toInt' : 'porPorcentajeIncapacidad' }").as("count");
        	group = Aggregation.group().count().as("count");
		} else if (op.equals(3)) {
			group = Aggregation.group().sum("numDiasSubsidiados").as("count");
		} 
        
        if (del != null) {
            return Aggregation.newAggregation(ChangeDTO.class, matchRFC, matchDate, matchDelPatron, matchSubDelPatron, matchFecBaja,  matchEstadoRegistro, matchIsPending, matchConsecuencia, matchCicloAnual, group);
		} else {
	        return Aggregation.newAggregation(ChangeDTO.class, matchRFC, matchDate, matchFecBaja,  matchEstadoRegistro, matchIsPending, matchConsecuencia, matchCicloAnual, group);
		}
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> totalRiesgos() {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, count);
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> contarTotalDefunciones() {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.CVE_CONSECUENCIA.getDesc()).is(CierreAnualImssConstants.CVE_CONSECUENCIA_DEFUNCION)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, count);
    }   
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> sumarTotalPorcentajes() {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);    	
    	CustomAggregationOperation sum = new CustomAggregationOperation("{ $group : { _id : null, porcentajeIncapacidad : { $sum : '$porcentajeIncapacidad' } } }");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, sum);
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> sumarTotalDias() {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);    	
    	    	
    	CustomAggregationOperation sum = new CustomAggregationOperation("{ $group : { _id : null, diasSubsidiados : { $sum : '$diasSubsidiados' } } }");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, sum);
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> contarTotalCorrectos(){
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(CierreAnualImssConstants.CVE_CORRECTOS)
    	));
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);    	
    	    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, count);
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> contarTotalCorrectosOtras() {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(CierreAnualImssConstants.CVE_CORRECTOS_OTRAS)
    	));
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, count);
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> contarTotalSusceptibles() {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(CierreAnualImssConstants.CVE_SUSCEPTIBLES)
    	));
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, count);    	
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> contarTotalSusceptiblesOtras() {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(CierreAnualImssConstants.CVE_SUSCEPTIBLES_OTRAS)
    	));
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
        
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, count);    	
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> contarCorrectos(Long key){
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(CierreAnualImssConstants.CVE_CORRECTOS),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, count);
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> contarCorrectosOtras(Long key) {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(CierreAnualImssConstants.CVE_CORRECTOS_OTRAS),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, count);
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> contarSusceptibles(Long key) {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(CierreAnualImssConstants.CVE_SUSCEPTIBLES),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, count);    	
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> contarSusceptiblesOtras(Long key) {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(CierreAnualImssConstants.CVE_SUSCEPTIBLES_OTRAS),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, count);    	
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> contarDefunciones(Long key) {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.CVE_CONSECUENCIA.getDesc()).is(CierreAnualImssConstants.CVE_CONSECUENCIA_DEFUNCION),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, count);
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> sumarPorcentajes(Long key) {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
    	
    	CustomAggregationOperation sum = new CustomAggregationOperation("{ $group : { _id : null, porcentajeIncapacidad : { $sum : '$porcentajeIncapacidad' } } }");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, sum);
    }
    
    public static TypedAggregation<RespaldoCierreAnualIMSSDTO> sumarDias(long key) {
    	String fecActual = DateUtils.obtenerMongoDateString();
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	String matchDateJSON = "{$match: { '" + BackupFieldsEnum.FEC_RESPALDO.getDesc() + "': { $lte: ISODate('" +
                fecActual.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
    	
    	CustomAggregationOperation sum = new CustomAggregationOperation("{ $group : { _id : null, diasSubsidiados : { $sum : '$diasSubsidiados' } } }");
    	
    	return Aggregation.newAggregation(RespaldoCierreAnualIMSSDTO.class, matchDate, matchOperation, sum);
    }

}
