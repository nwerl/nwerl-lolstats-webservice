package com.nwerl.lolstats.web.domain.match;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchReferenceRepository extends JpaRepository<MatchReference, Long> {
    public Page<MatchReference> findAllBySummoner_AccountId(String accountId, Pageable pageable);
    public List<MatchReference> findFirst20BySummoner_AccountIdAndGameCreationLessThanOrderByGameCreationDesc(String accountId, Long gameCreation);
    public List<MatchReference> findFirst20BySummoner_AccountIdOrderByGameCreationDesc(String accountId);
    public MatchReference findFirstBySummoner_AccountIdOrderByGameCreationDesc(String accountId);
}