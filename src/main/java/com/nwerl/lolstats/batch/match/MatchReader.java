package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.batch.MatchIdSet;
import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchReader implements ItemReader<RiotMatchDto> {
    private final MatchService matchService;
    private final MatchIdSet matchIdSet;

    @Override
    public RiotMatchDto read() {
        if (matchIdSet.noMatchesToUpdate())
            return null;
        log.info("matchIdSet size : {}", matchIdSet.getSize());
        Long nextMatchId = matchIdSet.getNextMatchId();

        RiotMatchDto nextMatchItem = matchService.fetchMatchFromRiotApi(nextMatchId);

        return nextMatchItem;
    }
}