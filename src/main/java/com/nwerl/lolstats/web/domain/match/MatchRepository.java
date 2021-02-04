package com.nwerl.lolstats.web.domain.match;

import com.mongodb.lang.Nullable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {
    //public Match findTopByParticipantIdentities_Player_AccountId(String accountId);
    public Boolean existsByGameId(Long gameId);
}