package com.nwerl.lolstats.batch.match;


import com.nwerl.lolstats.service.match.MatchReferenceCacheManager;
import com.nwerl.lolstats.service.match.MatchReferenceService;
import com.nwerl.lolstats.web.domain.match.*;
import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchWriter implements ItemWriter<Match> {
    private final MatchRepository matchRepository;
    private final MatchReferenceRepository matchReferenceRepository;
    private final SummonerRepository summonerRepository;
    private final MatchReferenceCacheManager matchReferenceCacheManager;

    @Transactional
    @Override
    public void write(List<? extends Match> items) {

        for(Match item : items) {
            matchRepository.save(item);

            for(Player player : item.getPlayers()) {
                String accountId = player.getAccountId();
                //DB에 없는 사용자일 경우, 패스한다
                if(!summonerRepository.existsByAccountId(accountId))
                    continue;

                Summoner summoner = summonerRepository.findByAccountId(accountId);
                MatchReference itemReference = MatchReference.builder()
                        .match(item).gameCreation(item.getGameCreation()).summoner(summoner).build();
                matchReferenceRepository.save(itemReference);

                Long firstMatchCacheGameCreation = matchReferenceCacheManager.getFirstMatchPage(accountId).get(0).getGameCreation();
                if(item.getGameCreation() >= firstMatchCacheGameCreation)
                    matchReferenceCacheManager.setFirstMatchPage(accountId);
            }
        }
    }
}
