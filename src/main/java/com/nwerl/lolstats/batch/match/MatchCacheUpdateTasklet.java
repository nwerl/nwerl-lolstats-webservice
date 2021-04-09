package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.dto.view.MatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@StepScope
@Component
public class MatchCacheUpdateTasklet implements Tasklet {
    private final RedisTemplate<String, MatchDto> redisTemplate;
    private final MatchService matchService;
    @Value("#{jobParameters['accountId']}")
    private String accountId;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ListOperations<String, MatchDto> listOps = redisTemplate.opsForList();

        Long gameId = (Long) chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .get("GAME_ID");

        MatchDto matchDto = matchService.getMatchByAccountId(accountId, gameId);

        listOps.rightPush(accountId, matchDto);

        return RepeatStatus.FINISHED;
    }
}
