package com.nwerl.lolstats.web.domain.match;

import com.nwerl.lolstats.web.domain.custom.CustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MatchListRepository extends MongoRepository<MatchList, String>, CustomRepository {
    public List<MatchList> findAll();
    public MatchList findByAccountId(String Id);
    public Boolean existsByAccountId(String Id);

    @Query(value = "{_id: ?0}", fields = "{matchReferences: {$slice: [?1, ?2]}}")
    public MatchList findByAccountId(String id, int skip, int limit);
    public void matchListFindAndModify(String accountId, MatchReference matchReference);
}