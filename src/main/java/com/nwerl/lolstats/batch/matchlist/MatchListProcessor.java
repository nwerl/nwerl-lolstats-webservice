package com.nwerl.lolstats.batch.matchlist;


import com.nwerl.lolstats.batch.MatchIdSet;
import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.domain.match.MatchList;
import com.nwerl.lolstats.web.domain.match.MatchReference;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchListProcessor implements ItemProcessor<RiotMatchReferenceDto, MatchList> {
    private final MatchService matchService;
    private final MatchIdSet matchIdSet;

    @Override
    public MatchList process(RiotMatchReferenceDto item) throws Exception {
        Long gameId = item.getGameId();
        Long timeStamp = item.getTimestamp();

        if(!matchService.existsByGameId(gameId)) {
            log.info("{}'s GAME {} NOT EXISTS IN DB", item.getAccountId(), gameId);
            matchIdSet.addMatchId(gameId);
        }

        return MatchList.builder().accountId(item.getAccountId()).matchReferences(Collections.singletonList(new MatchReference(gameId, timeStamp))).build();
    }
}