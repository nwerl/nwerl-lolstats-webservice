package com.nwerl.lolstats.web.domain.champion;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChampionRepository extends MongoRepository<Champion, Long> {
}
