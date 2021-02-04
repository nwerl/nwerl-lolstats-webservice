package com.nwerl.lolstats.web.domain.league;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeagueItemRepository extends MongoRepository<LeagueItem, String> {
}