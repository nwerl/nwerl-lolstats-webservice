package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchReader implements ItemReader<RiotMatchDto> {
    private final MatchService matchService;
    private StepExecution stepExecution;

    private Long gameId;

    @Override
    public RiotMatchDto read() throws InterruptedException {
        if (thisGameIdIsUpdated())
            return null;

        gameId = getGameId();

        return fetchMatchFromRiotApi(gameId);
    }

    public Long getGameId() {
        ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();

        return jobExecutionContext.getLong("GAME_ID");
    }

    private boolean thisGameIdIsUpdated() {
        return gameId != null;
    }

    public RiotMatchDto fetchMatchFromRiotApi(Long gameId) throws InterruptedException {
        try {
            return matchService.fetchMatchFromRiotApi(gameId);
        } catch (HttpClientErrorException.TooManyRequests e){
            Thread.sleep(120000 + 20000);
            return matchService.fetchMatchFromRiotApi(gameId);
        }

    }

    @BeforeStep
    public void setUp(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}