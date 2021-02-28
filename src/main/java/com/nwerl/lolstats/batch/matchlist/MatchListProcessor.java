package com.nwerl.lolstats.batch.matchlist;

import com.nwerl.lolstats.service.match.MatchService;
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

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchListProcessor implements ItemProcessor<RiotMatchReferenceDto, MatchReference> {
    private final MatchService matchService;
    private StepExecution stepExecution;

    @Override
    public MatchReference process(RiotMatchReferenceDto item) throws Exception {
        Long gameId = item.getGameId();
        Long timeStamp = item.getTimestamp();

        log.info("gameId : {}", gameId);

        if(matchService.existsByGameId(gameId)) {
            log.info("MATCH EXISTS IN DB");
            stepExecution.setExitStatus(ExitStatus.FAILED);
        }


        stepExecution.getJobExecution().getExecutionContext().put("GAME_ID", gameId);

        return new MatchReference(gameId, timeStamp);
    }

    @BeforeStep
    public void setGameId(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}