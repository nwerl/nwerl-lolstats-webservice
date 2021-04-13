package com.nwerl.lolstats.web.domain.league;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueItemRepository extends JpaRepository<LeagueItem, Long> {
    public Page<LeagueItem> findAll(Pageable pageable);
    public Page<LeagueItem> findAllByOrderByLeaguePointsDesc(Pageable pageable);
    public void deleteBySummonerId(String summonerId);
    public void deleteInBatch(Iterable<LeagueItem> entities);
}