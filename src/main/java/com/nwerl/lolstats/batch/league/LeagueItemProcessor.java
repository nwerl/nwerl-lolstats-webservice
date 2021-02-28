package com.nwerl.lolstats.batch.league;

import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

@Slf4j
@StepScope
@Configuration
public class LeagueItemProcessor implements ItemProcessor<RiotLeagueItemDto, LeagueItem>{
    @Override
    public LeagueItem process(RiotLeagueItemDto item) throws Exception {
        return item.toEntity();
    }
}