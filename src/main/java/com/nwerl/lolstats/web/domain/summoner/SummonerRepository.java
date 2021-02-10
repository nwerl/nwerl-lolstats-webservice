package com.nwerl.lolstats.web.domain.summoner;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SummonerRepository extends MongoRepository<Summoner, String> {
    public List<Summoner> findAll();
    public Summoner findByName(String name);
    public Optional<Summoner> findById(String id);
    public Boolean existsByName(String name);
}