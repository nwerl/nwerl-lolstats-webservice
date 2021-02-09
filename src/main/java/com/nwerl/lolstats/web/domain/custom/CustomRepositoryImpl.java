package com.nwerl.lolstats.web.domain.custom;

import com.nwerl.lolstats.web.domain.match.MatchList;
import com.nwerl.lolstats.web.domain.match.MatchReference;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CustomRepositoryImpl implements CustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void customMethod(String accountId, MatchReference matchReference) {
        Query query = new Query(Criteria.where("_id").is(accountId));
        Update update = new Update().addToSet("matchReferences", matchReference);

        mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().upsert(true), MatchList.class);
    }
}
