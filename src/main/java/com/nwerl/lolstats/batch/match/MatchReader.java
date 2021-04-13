package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchReader implements ItemReader<RiotMatchDto> {
    private final RetryTemplate retryTemplate;
    private final MatchService matchService;

    @Value("#{jobParameters['accountId']}")
    private String accountId;

    private Boolean THIS_ACCOUNT_ID_IS_UPDATED = false;

    @Override
    public RiotMatchDto read() {
        if(THIS_ACCOUNT_ID_IS_UPDATED)
            return null;

        RiotMatchReferenceDto matchReference = getLastMatchReference(accountId);

        if(matchReference == null) {
            return null;
        }

        RiotMatchDto matchDto = getMatch(matchReference.getGameId());

        THIS_ACCOUNT_ID_IS_UPDATED = true;

        return matchDto;
    }


    private RiotMatchReferenceDto getLastMatchReference(String accountId) {
        return retryTemplate.execute(arg -> matchService.fetchLastRankMatchReferenceFromRiotApi(accountId));
    }

    private RiotMatchDto getMatch(Long gameId) {
        return retryTemplate.execute(arg -> matchService.fetchMatchFromRiotApi(gameId));
    }
}