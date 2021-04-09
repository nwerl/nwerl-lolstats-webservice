package com.nwerl.lolstats.web.domain.league;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeagueItemRepository extends MongoRepository<LeagueItem, String> {
    public Page<LeagueItem> findAll(Pageable pageable);
    public Page<LeagueItem> findAllByOrderByLeaguePointsDesc(Pageable pageable);
    public void deleteBySummonerId(String summonerId);
    public void deleteInBatchBySummonerId(Iterable<LeagueItem> entities);
}