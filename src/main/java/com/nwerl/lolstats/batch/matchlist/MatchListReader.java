package com.nwerl.lolstats.batch.matchlist;

import com.nwerl.lolstats.service.RiotApiRequestService;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.MatchReferenceDto;
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
public class MatchListReader implements ItemReader<MatchReferenceDto> {
    private final RiotApiRequestService riotApiRequestService;

    @Value("#{jobParameters[accountId]}")
    private String accountId;
    private Boolean flag = false;

    @Override
    public MatchReferenceDto read() throws Exception{
        if(!flag){
            flag = true;
            log.info("accountId : {}", accountId);
            return riotApiRequestService.getLastMatchReference(accountId);
        }
        else {
            return null;
        }
    }
}