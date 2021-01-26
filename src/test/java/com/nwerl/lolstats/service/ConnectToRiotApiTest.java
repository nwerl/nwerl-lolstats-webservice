package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.dto.SummonerDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectToRiotApiTest {
    @Autowired
    private RiotApiRequestService riotApiRequestService;

    @Test
    public void connect_to_Riot_Api() {
        String name = "Vehumet";
        SummonerDto summonerDto = riotApiRequestService.getSummonerInfoByName(name);
        assertThat(name, is(summonerDto.getName()));
    }
}
