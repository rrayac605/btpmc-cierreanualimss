package mx.gob.imss.cit.pmc.cierreanualimss.repository.impl;

import mx.gob.imss.cit.pmc.cierreanualimss.dto.RespaldoCierreAnualIMSSDTO;
import mx.gob.imss.cit.pmc.cierreanualimss.enums.BackupFieldsEnum;
import mx.gob.imss.cit.pmc.cierreanualimss.repository.BackupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class BackupRepositoryImpl implements BackupRepository {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void deleteByKey(Long key) {
        Query query = new Query(Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key));
        mongoOperations.remove(query, RespaldoCierreAnualIMSSDTO.class);
    }

}
