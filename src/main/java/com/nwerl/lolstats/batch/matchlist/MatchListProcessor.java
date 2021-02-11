package com.nwerl.lolstats.batch.matchlist;

import com.nwerl.lolstats.service.MatchService;
import com.nwerl.lolstats.web.domain.match.MatchReference;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.RiotMatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchListProcessor implements ItemProcessor<RiotMatchReferenceDto, MatchReference> {
    private final MatchService matchService;
    private StepExecution stepExecution;

    @Override
    public MatchReference process(RiotMatchReferenceDto item) throws Exception {
        log.info("gameId : {}", item.getGameId());

        if(matchService.existsByGameId(item.getGameId())) {
            log.info("MATCH EXISTS IN DB");
            stepExecution.setExitStatus(ExitStatus.FAILED);
        }


        stepExecution.getJobExecution().getExecutionContext().put("GAME_ID", item.getGameId());

        return new MatchReference(item.getGameId(), item.getTimestamp());
    }

    @BeforeStep
    public void setGameId(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}