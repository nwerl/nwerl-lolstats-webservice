package com.nwerl.lolstats.web.domain.match;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MatchReferenceRepository extends MongoRepository<MatchReference, Integer> {
    public List<MatchReference> findAll();
}
