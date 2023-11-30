package mx.gob.imss.cit.pmc.cierreanualimss.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.cierreanualimss.dto.CountDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.CountRepository;

@Repository
public class CountRepositoryImpl implements CountRepository {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public long count(TypedAggregation<?> aggregation) {
        CountDTO countDTO = mongoOperations.aggregate(aggregation, CountDTO.class).getUniqueMappedResult();
        return countDTO != null && countDTO.getCount() != null ? countDTO.getCount() : 0L;
    }

	@Override
	public CountDTO totales(TypedAggregation<?> aggregation) {
		return mongoOperations.aggregate(aggregation, CountDTO.class).getUniqueMappedResult();
	}
	
	@Override
	public Document getAgregation(String aggregation) {
		return mongoOperations.executeCommand(aggregation);
	}
}
