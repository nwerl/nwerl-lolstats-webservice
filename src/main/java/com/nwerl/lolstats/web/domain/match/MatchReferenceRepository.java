package com.nwerl.lolstats.web.domain.match;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchReferenceRepository extends JpaRepository<MatchReference, Long> {
    public Page<MatchReference> findAllBySummoner_AccountId(String accountId, Pageable pageable);
}