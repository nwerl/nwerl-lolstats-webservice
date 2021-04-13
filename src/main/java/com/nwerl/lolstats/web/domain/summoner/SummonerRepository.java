package com.nwerl.lolstats.web.domain.summoner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SummonerRepository extends JpaRepository<Summoner, Long> {
    public List<Summoner> findAll();
    public Summoner findByName(String name);
    public Summoner findByAccountId(String accountId);
    public Optional<Summoner> findBySummonerId(String summonerId);
    public Boolean existsBySummonerId(String summonerId);
    public Boolean existsByAccountId(String accountId);
}