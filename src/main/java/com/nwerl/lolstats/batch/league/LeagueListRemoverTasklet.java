package com.nwerl.lolstats.batch.league;

import com.nwerl.lolstats.service.league.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LeagueListRemoverTasklet implements Tasklet {
    private final LeagueService leagueService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        leagueService.updateLeagueRanking();

        return RepeatStatus.FINISHED;
    }
}
