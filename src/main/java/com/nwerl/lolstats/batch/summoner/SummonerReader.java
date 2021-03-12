package com.nwerl.lolstats.batch.summoner;

import com.nwerl.lolstats.service.league.LeagueService;
import com.nwerl.lolstats.service.summoner.SummonerService;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import com.nwerl.lolstats.web.dto.riotapi.summoner.RiotSummonerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


@Slf4j
@StepScope
@Configuration
public class SummonerReader implements ItemReader<RiotSummonerDto> {
    private final SummonerService summonerService;

    private Queue<String> summonerQueue;

    public SummonerReader(LeagueService leagueService,
                          SummonerService summonerService) {
        this.summonerService = summonerService;
        this.summonerQueue = new LinkedList<>();

        setSummonerQueue(leagueService);
    }

    @Override
    public RiotSummonerDto read() throws Exception{
        String nextSummonerId = summonerQueue.poll();

        if(nextSummonerId == null)
            return null;


        return summonerService.fetchSummonerFromRiotApiById(nextSummonerId);
    }

    private void setSummonerQueue(LeagueService leagueService) {
        //Summoner 컬렉션에 없는 summonerId만 Queue에 담는다.
        List<RiotLeagueItemDto> list = leagueService.findAll().getEntries();
        for(RiotLeagueItemDto item : list) {
            if(!summonerService.checkByName(item.getSummonerName(), item.getSummonerId())) {
                summonerQueue.add(item.getSummonerId());
            }
        }
        log.info("notExistsSummonerQueue Size : {}", summonerQueue.size());
    }
}
