package com.nwerl.lolstats.batch.league;

import com.nwerl.lolstats.service.league.LeagueApiCaller;
import com.nwerl.lolstats.service.league.LeagueService;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


@Slf4j
@RequiredArgsConstructor
@StepScope
@Configuration
public class LeagueItemReader implements ItemReader<RiotLeagueItemDto> {
    private final LeagueApiCaller leagueApiCaller;

    private Queue<RiotLeagueItemDto> challengerLeagueItemQueue;

    @Override
    public RiotLeagueItemDto read() throws Exception{
        if(challengerLeagueItemQueueIsNotInitialized()) {
            this.challengerLeagueItemQueue = new LinkedList<>(fetchChallengerLeagueItemsFromRiotApi());
        }

        RiotLeagueItemDto nextLeagueItem = challengerLeagueItemQueue.poll();

        return nextLeagueItem;
    }

    private List<RiotLeagueItemDto> fetchChallengerLeagueItemsFromRiotApi() {
        return leagueApiCaller.fetchChallengerLeagueListFromRiotApi().getEntries();
    }

    private Boolean challengerLeagueItemQueueIsNotInitialized() {
        return this.challengerLeagueItemQueue == null;
    }
}
