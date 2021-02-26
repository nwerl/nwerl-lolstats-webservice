package com.nwerl.lolstats.batch.league;

import com.nwerl.lolstats.service.league.LeagueApiCaller;
import com.nwerl.lolstats.service.league.LeagueService;
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
    private final LeagueApiCaller leagueApiCaller;
    private Boolean readFlag;

    @Autowired
    public LeagueItemReader(LeagueApiCaller leagueApiCaller) {
        this.leagueApiCaller = leagueApiCaller;
        this.readFlag = false;
    }


    @Override
    public List<LeagueItemDto> read() throws Exception{
        if(readFlag) {
            return null;
        }
        else {
            readFlag = true;
//            try {
//                return leagueService.callApiChallengerLeagueItem().getEntries();
//            }
//            catch(final HttpClientErrorException e) {
//                log.error("{} Error : {}", e.getStatusCode(), e.getStatusText());
//                return Collections.emptyList();
//            }
            return leagueApiCaller.callApiChallengerLeagueItem().getEntries();
        }
    }
}
