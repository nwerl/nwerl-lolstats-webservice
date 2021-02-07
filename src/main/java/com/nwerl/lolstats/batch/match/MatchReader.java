package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.service.MatchService;
import com.nwerl.lolstats.web.dto.riotApi.match.MatchDto;
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
public class MatchReader implements ItemReader<MatchDto> {
    private final MatchService matchService;
    private Long gameId;
    private Boolean flag = false;

    @Override
    public MatchDto read() throws Exception {
        if(!flag){
            flag = true;
            return matchService.callApiMatchByGameId(gameId);
        }
        else {
            return null;
        }

    }

    @BeforeStep
    public void getGameId(StepExecution stepExecution) {
        ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
        gameId = jobExecutionContext.getLong("GAME_ID");
    }
}