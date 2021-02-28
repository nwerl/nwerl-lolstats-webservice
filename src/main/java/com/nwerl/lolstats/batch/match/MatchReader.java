package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.service.match.MatchApiCaller;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchReader implements ItemReader<RiotMatchDto> {
    private final MatchApiCaller matchApiCaller;

    private Long gameId;
    private RiotMatchDto matchItem;

    @BeforeStep
    public void setGameId(StepExecution stepExecution) {
        ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
        gameId = jobExecutionContext.getLong("GAME_ID");
    }

    @Override
    public RiotMatchDto read() throws Exception {
        if(!matchItemIsNotInitialized())
            return null;

        matchItem = matchApiCaller.fetchMatchFromRiotApi(this.gameId);
        return matchItem;
    }

    private Boolean matchItemIsNotInitialized() {
        return matchItem == null;
    }
}