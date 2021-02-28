package com.nwerl.lolstats.batch.league;

import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.league.LeagueItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class LeagueItemWriter implements ItemWriter<LeagueItem> {
    private final LeagueItemRepository leagueItemRepository;

    @Override
    public void write(List<? extends LeagueItem> items) {
        leagueItemRepository.saveAll(items);
    }
}