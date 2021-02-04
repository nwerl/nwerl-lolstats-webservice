package com.nwerl.lolstats.batch.league;

import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@StepScope
@Configuration
public class LeagueItemProcessor implements ItemProcessor<List<LeagueItemDto>, List<LeagueItem>> {
    @Override
    public List<LeagueItem> process(List<LeagueItemDto> item) throws Exception {
        return item.stream().map(LeagueItemDto::toEntity).collect(Collectors.toList());
    }
}
