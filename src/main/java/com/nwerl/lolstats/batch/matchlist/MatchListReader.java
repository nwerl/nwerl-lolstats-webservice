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
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;


@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchListReader implements ItemReader<RiotMatchReferenceDto> {
    private final MatchService matchService;

    @Value("#{jobParameters['accountId']}")
    private String accountId;
    private StepExecution stepExecution;

    private static final String THIS_ACCOUNT_ID_IS_UPDATED = "";

    @Override
    public RiotMatchReferenceDto read() throws InterruptedException {
        if(thisAccountIdIsUpdated())
            return null;

        RiotMatchReferenceDto matchReference = getLastMatchReference(accountId);

        if(matchReference == null)
            stepExecution.setExitStatus(ExitStatus.FAILED);

        accountId = THIS_ACCOUNT_ID_IS_UPDATED;

        return matchReference;
    }

    private RiotMatchReferenceDto getLastMatchReference(String accountId) throws InterruptedException {
        try {
            return matchService.fetchLastRankMatchReferenceFromRiotApi(accountId);
        } catch (HttpClientErrorException.TooManyRequests e) {
            Thread.sleep(120000 + 20000);
            return matchService.fetchLastRankMatchReferenceFromRiotApi(accountId);
        }
    }

    private Boolean thisAccountIdIsUpdated() {
        return accountId.trim().isEmpty();
    }

    @BeforeStep
    public void setUp(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}