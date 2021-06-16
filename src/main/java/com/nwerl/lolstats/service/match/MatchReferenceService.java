package com.nwerl.lolstats.service.match;

import com.nwerl.lolstats.web.domain.match.MatchReferenceRepository;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.view.MatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchReferenceService {
    private final MatchReferenceRepository matchReferenceRepository;
    private final MatchReferenceCacheManager matchReferenceCacheManager;
    private final SummonerRepository summonerRepository;

    public static final long NOT_INIT_GAMECREATION = Long.MAX_VALUE;
    public static final int UPDATE_PAGE_COUNT = 3;

    public List<MatchDto> getMatchPageBySummonerName(String summonerName, Long gameCreation) {
        String accountId = summonerRepository.findByName(summonerName).getAccountId();

        if(gameCreation == NOT_INIT_GAMECREATION)
            gameCreation = matchReferenceRepository.findFirstBySummoner_AccountIdOrderByGameCreationDesc(accountId).getGameCreation() + 1;

        return matchReferenceCacheManager.getMatchPageWithNoOffset(accountId, gameCreation);
    }

    public void updateMatchPage(String accountId, Long gameCreation) {
        AtomicInteger updatePageCount = new AtomicInteger(UPDATE_PAGE_COUNT);

        while(updatePageCount.getAndDecrement() > 0 && gameCreation != NOT_INIT_GAMECREATION) {
            List<MatchDto> matchPage = matchReferenceCacheManager.getMatchPageWithNoOffset(accountId, gameCreation);
            gameCreation = matchPage.size() > 0 ? matchPage.get(matchPage.size()-1).getGameCreation() : NOT_INIT_GAMECREATION;
        }
    }
}
