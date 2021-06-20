package com.nwerl.lolstats.service.match;


import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.view.MatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchReferenceService {
    private final MatchReferenceCacheManager matchReferenceCacheManager;
    private final SummonerRepository summonerRepository;

    public static final long NOT_INIT_GAMECREATION = Long.MAX_VALUE;

    public List<MatchDto> getMatchPageBySummonerName(String summonerName, Long gameCreation) {
        String accountId = summonerRepository.findByName(summonerName).getAccountId();

        if(gameCreation == NOT_INIT_GAMECREATION)
            return matchReferenceCacheManager.getFirstMatchPage(accountId);

        return matchReferenceCacheManager.getMatchPageWithNoOffset(accountId, gameCreation);
    }
}
