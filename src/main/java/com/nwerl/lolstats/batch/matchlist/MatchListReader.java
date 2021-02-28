package com.nwerl.lolstats.batch.matchlist;

import com.nwerl.lolstats.service.match.MatchApiCaller;
import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.domain.match.MatchReference;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchListReader implements ItemReader<RiotMatchReferenceDto> {
    private final MatchApiCaller matchApiCaller;

    @Value("#{jobParameters[accountId]}")
    private String accountId;
    private RiotMatchReferenceDto lastMatchReferenceItem;

    @Override
    public RiotMatchReferenceDto read() throws Exception{
        if(!lastMatchReferenceItemIsNotInitialized())
            return null;

        lastMatchReferenceItem = getLastMatchReference();
        return lastMatchReferenceItem;
    }

    private RiotMatchReferenceDto getLastMatchReference() {
        return matchApiCaller.fetchMatchListFromRiotApi(this.accountId).getMatches().get(0);
    }

    private Boolean lastMatchReferenceItemIsNotInitialized() {
        return lastMatchReferenceItem == null;
    }
}