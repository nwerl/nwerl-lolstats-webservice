package com.nwerl.lolstats.batch.league;

import com.nwerl.lolstats.service.LeagueService;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Slf4j
@StepScope
@Configuration
public class LeagueItemReader implements ItemReader<List<LeagueItemDto>> {
    private final LeagueService leagueService;
    private Boolean readFlag;

    @Autowired
    public LeagueItemReader(LeagueService leagueService) {
        this.leagueService = leagueService;
        this.readFlag = false;
    }


    @Override
    public List<LeagueItemDto> read() throws Exception{
        if(readFlag) {
            return null;
        }
        else {
            readFlag = true;
            return leagueService.getChallengerLeagueItem().getEntries();
        }
    }
}
