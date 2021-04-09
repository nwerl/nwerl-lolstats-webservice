package com.nwerl.lolstats.batch.matchlist;


import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.domain.match.MatchList;
import com.nwerl.lolstats.web.domain.match.MatchReference;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
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

    private StepExecution stepExecution;


    @Override
    public MatchList process(RiotMatchReferenceDto item) throws Exception {
        Long gameId = item.getGameId();

        if(!matchService.existsByGameId(gameId)) {
            log.info("{}'s GAME {} NOT EXISTS IN DB", item.getAccountId(), gameId);
        }
        else {
            stepExecution.setExitStatus(ExitStatus.FAILED);
        }

        stepExecution.getJobExecution().getExecutionContext().put("GAME_ID", gameId);

        return MatchList.builder().accountId(item.getAccountId()).matchReferences(Collections.singletonList(new MatchReference(gameId, item.getTimestamp()))).build();
    }

    @BeforeStep
    public void setUp(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}