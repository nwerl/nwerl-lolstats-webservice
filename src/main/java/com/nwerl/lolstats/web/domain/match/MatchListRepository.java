package com.nwerl.lolstats.web.domain.match;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MatchListRepository extends MongoRepository<MatchList, String> {
    public List<MatchList> findAll();
    public MatchList findByAccountId(String Id);
    public Boolean existsByAccountId(String Id);
}
