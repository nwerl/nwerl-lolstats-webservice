package com.nwerl.lolstats.web.domain.match;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeaturedGameInfoRepository extends MongoRepository<FeaturedGameInfo, String> {
}
