package mx.gob.imss.cit.pmc.cierreanualimss.repository;

import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.bson.Document;

import mx.gob.imss.cit.pmc.cierreanualimss.dto.CountDTO;

public interface CountRepository {

    long count(TypedAggregation<?> aggregation);
    
    CountDTO totales(TypedAggregation<?> aggregation);
    
    Document getAgregation(String aggregation);

}
