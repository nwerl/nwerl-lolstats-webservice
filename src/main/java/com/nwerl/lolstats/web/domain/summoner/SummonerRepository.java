package com.nwerl.lolstats.web.domain.summoner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SummonerRepository extends MongoRepository<Summoner, String> {
    public List<Summoner> findAll();
    public List<Summoner> findByName(String name);
}
