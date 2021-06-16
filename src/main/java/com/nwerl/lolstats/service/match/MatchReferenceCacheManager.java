package com.nwerl.lolstats.service.match;

import com.nwerl.lolstats.web.domain.match.MatchReferenceRepository;
import com.nwerl.lolstats.web.dto.view.MatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class MatchReferenceCacheManager {
    private final MatchReferenceRepository matchReferenceRepository;

    @Cacheable(value="match", key="#accountId.concat('-').concat(#gameCreation)")
    public List<MatchDto> getMatchPageWithNoOffset(String accountId, Long gameCreation) {
        return matchReferenceRepository.findFirst20BySummoner_AccountIdAndGameCreationLessThanOrderByGameCreationDesc(accountId, gameCreation)
                .stream().map(m -> m.getMatch().of(accountId)).collect(Collectors.toList());
    }

    @CachePut(value="match", key="#accountId.concat('-').concat(#gameCreation)")
    public List<MatchDto> setMatchPageWithNoOffset(String accountId, Long gameCreation) {
        return matchReferenceRepository.findFirst20BySummoner_AccountIdAndGameCreationLessThanOrderByGameCreationDesc(accountId, gameCreation)
                .stream().map(m -> m.getMatch().of(accountId)).collect(Collectors.toList());
    }
}
