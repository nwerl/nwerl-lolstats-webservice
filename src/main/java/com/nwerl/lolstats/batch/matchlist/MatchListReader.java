package com.nwerl.lolstats.batch.matchlist;

import com.nwerl.lolstats.service.match.MatchApiCaller;
import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.RiotMatchReferenceDto;
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
    private Boolean flag = false;

    @Override
    public RiotMatchReferenceDto read() throws Exception{
        if(!flag){
            flag = true;
            log.info("accountId : {}", accountId);
            return matchApiCaller.callApiLastMatchReference(accountId);
        }
        else {
            return null;
        }
    }
}