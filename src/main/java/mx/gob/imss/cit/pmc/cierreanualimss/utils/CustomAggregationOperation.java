package mx.gob.imss.cit.pmc.cierreanualimss.utils;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

import com.mongodb.DBObject;

public class CustomAggregationOperation implements AggregationOperation {
	
	private String jsonOpperation;
    private DBObject query;

	
	public CustomAggregationOperation(String jsonOpperaton) {
		this.jsonOpperation = jsonOpperaton;
	}

	public CustomAggregationOperation(String jsonOpperation, DBObject query) {
		super();
		this.jsonOpperation = jsonOpperation;
		this.query = query;
	}

	@Override
	public Document toDocument(AggregationOperationContext context) {
		return context.getMappedObject(Document.parse(jsonOpperation));
	}	

}
