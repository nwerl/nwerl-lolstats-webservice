package com.nwerl.lolstats.service.league;


import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueListDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class LeagueApiCallerRetryTest {
    @Autowired
    private LeagueApiCaller leagueApiCaller;

    @Test
    public void MockitoLeagueApiCaller_5xxFail_Test(){
        RiotLeagueListDto list = leagueApiCaller.fetchChallengerLeagueListFromRiotApi();
        assertThat(list.getIndex(), is(1));
    }

    @Configuration
    @EnableRetry
    public static class SpringConfig {
        @Bean
        public LeagueApiCaller leagueApiCall() throws Exception {
            LeagueApiCaller leagueApiCaller = mock(LeagueApiCaller.class);

            when(leagueApiCaller.fetchChallengerLeagueListFromRiotApi())
                    .thenThrow(HttpServerErrorException.class)
                    .thenThrow(HttpServerErrorException.class)
                    .thenReturn(new RiotLeagueListDto(Collections.emptyList(), 1));

            return leagueApiCaller;
        }
    }
}
