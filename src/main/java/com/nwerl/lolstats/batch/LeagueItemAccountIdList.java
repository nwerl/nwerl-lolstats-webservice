package com.nwerl.lolstats.batch;

import com.nwerl.lolstats.service.league.LeagueService;
import com.nwerl.lolstats.service.summoner.SummonerService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Component
public class LeagueItemAccountIdList {
    private final LeagueService leagueService;
    private final SummonerService summonerService;

    private List<String> accountIdList;
    private ListIterator<String> it;

    public LeagueItemAccountIdList(LeagueService leagueService,
                                   SummonerService summonerService) {
        this.leagueService = leagueService;
        this.summonerService = summonerService;

        update();
    }


    public String getNextAccountId() {
        if(!it.hasNext())
            it = accountIdList.listIterator();

        return it.next();
    }

    public void update() {
        accountIdList = leagueService.findAll().getEntries().stream()
                .map(item -> summonerService.findAccountIdById(item.getSummonerId()))
                .collect(Collectors.toList());

        it = accountIdList.listIterator();
    }
}
