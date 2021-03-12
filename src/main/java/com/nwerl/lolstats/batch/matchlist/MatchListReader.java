package com.nwerl.lolstats.batch.matchlist;

import com.nwerl.lolstats.batch.LeagueItemAccountIdList;
import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;


@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchListReader implements ItemReader<RiotMatchReferenceDto> {
    private final MatchService matchService;
    private final LeagueItemAccountIdList leagueItemAccountIdList;
    private StepExecution stepExecution;

    private String accountId;

    @Override
    public RiotMatchReferenceDto read() throws InterruptedException {
        if(thisAccountIdIsUpdated())
            return null;

        accountId = leagueItemAccountIdList.getNextAccountId();
        RiotMatchReferenceDto matchReference = getLastMatchReference(accountId);

        if(matchReference == null)
            stepExecution.setExitStatus(ExitStatus.FAILED);

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
        return accountId != null;
    }

    @BeforeStep
    public void setUp(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}