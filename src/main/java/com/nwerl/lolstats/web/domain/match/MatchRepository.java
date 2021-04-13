package com.nwerl.lolstats.web.domain.match;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    //public Match findTopByParticipantIdentities_Player_AccountId(String accountId);
    public Optional<Match> findById(Long id);
}