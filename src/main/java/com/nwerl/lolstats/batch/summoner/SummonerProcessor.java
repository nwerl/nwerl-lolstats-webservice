package com.nwerl.lolstats.batch.summoner;

import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.dto.riotapi.summoner.RiotSummonerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

@Slf4j
@StepScope
@Configuration
public class SummonerProcessor implements ItemProcessor<RiotSummonerDto, Summoner> {
    @Override
    public Summoner process(RiotSummonerDto item) throws Exception {
        //Thread.sleep(1400);
        return item.toEntity();
    }
}
