package com.nwerl.lolstats.web.domain.match;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MatchRepository extends MongoRepository<Match, Long> {
    //public Match findTopByParticipantIdentities_Player_AccountId(String accountId);
    public Boolean existsByGameId(Long gameId);
    public Optional<Match> findById(Long id);
}