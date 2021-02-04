package com.nwerl.lolstats.web.domain.featuredgame;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeaturedGameInfoRepository extends MongoRepository<FeaturedGameInfo, String> {
}
