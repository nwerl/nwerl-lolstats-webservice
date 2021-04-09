package com.nwerl.lolstats.batch.matchlist;

import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchListReader implements ItemReader<RiotMatchReferenceDto> {
    private final RetryTemplate retryTemplate;
    private final MatchService matchService;

    @Value("#{jobParameters['accountId']}")
    private String accountId;
    private StepExecution stepExecution;

    private Boolean THIS_ACCOUNT_ID_IS_UPDATED = false;

    @Override
    public RiotMatchReferenceDto read() throws InterruptedException {
        if(THIS_ACCOUNT_ID_IS_UPDATED)
            return null;

        RiotMatchReferenceDto matchReference = getLastMatchReference(accountId);

        if(matchReference == null)
            stepExecution.setExitStatus(ExitStatus.FAILED);

        THIS_ACCOUNT_ID_IS_UPDATED = true;

        return matchReference;
    }


    private RiotMatchReferenceDto getLastMatchReference(String accountId) throws InterruptedException {
        return retryTemplate.execute(arg -> matchService.fetchLastRankMatchReferenceFromRiotApi(accountId));
    }

    @BeforeStep
    public void setUp(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}