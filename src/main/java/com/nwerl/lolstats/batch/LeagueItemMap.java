package com.nwerl.lolstats.batch;

import com.nwerl.lolstats.service.league.LeagueService;
import com.nwerl.lolstats.service.summoner.SummonerService;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LeagueItemMap {
    private final LeagueService leagueService;
    private final SummonerService summonerService;

    private Map<String, String> summonerNameList;
    private Iterator<String> it;

    public LeagueItemMap(LeagueService leagueService,
                         SummonerService summonerService) {
        this.leagueService = leagueService;
        this.summonerService = summonerService;
        this.summonerNameList = new HashMap<>();
    }


    public String getNextSummonerName() {
        if(!it.hasNext())
            it = summonerNameList.keySet().iterator();

        return it.next();
    }

    public String getAccountId(String summonerName) {
        return summonerNameList.get(summonerName);
    }

    public void update() {
        summonerNameList = leagueService.findAll().getEntries().stream()
                .collect(Collectors.toMap(RiotLeagueItemDto::getSummonerName,
                        item -> summonerService.findAccountIdById(item.getSummonerId())));
        //todo : 이부분 summonerName으로 찾으면 에러나는거 봐서는 닉변수정이 바로 안되는듯함

        it = summonerNameList.keySet().iterator();
    }
}
