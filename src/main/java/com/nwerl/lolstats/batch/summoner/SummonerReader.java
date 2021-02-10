package com.nwerl.lolstats.batch.summoner;

import com.nwerl.lolstats.service.LeagueService;
import com.nwerl.lolstats.service.SummonerService;
import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


@Slf4j
@StepScope
@Configuration
public class SummonerReader implements ItemReader<SummonerDto> {
    private final SummonerService summonerService;
    private Queue<String> notExistsSummonerQueue;

    @Autowired
    public SummonerReader(LeagueService leagueService,
                          SummonerService summonerService) {
        this.summonerService = summonerService;
        this.notExistsSummonerQueue = new LinkedList<>();

        List<LeagueItemDto> list = leagueService.findAll().getEntries();
        for(LeagueItemDto item : list) {
            if(!summonerService.existsById(item.getSummonerId()))
                notExistsSummonerQueue.add(item.getSummonerId());
        }
        log.info("notExistsSummonerQueue Size : {}", notExistsSummonerQueue.size());
    }

    @Override
    public SummonerDto read() throws Exception{
        if(notExistsSummonerQueue.isEmpty())
            return null;
        else {
            Thread.sleep(1400);
            return summonerService.callApiSummonerInfoBySummonerId(notExistsSummonerQueue.poll());
        }
    }
}
