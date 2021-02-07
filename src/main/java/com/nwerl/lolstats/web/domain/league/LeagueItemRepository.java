package com.nwerl.lolstats.web.domain.league;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LeagueItemRepository extends MongoRepository<LeagueItem, String> {
    public List<LeagueItem> findAllByOrderByLeaguePointsDesc();
    public void deleteBySummonerId(String summonerId);
}