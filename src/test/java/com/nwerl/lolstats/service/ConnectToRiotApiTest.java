package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectToRiotApiTest {
    @Autowired
    private SummonerService summonerService;
    @Autowired
    private DDragonService dDragonService;
    @Autowired
    private DDragonApiCaller dDragonApiCaller;

    @Test
    public void connect_to_Riot_Api() {
        String name = "Vehumet";
        SummonerDto summonerDto = summonerService.callApiSummonerInfoByName(name);
        assertThat(name, is(summonerDto.getName()));
    }

    @Test
    public void update_Champions_Test() throws IOException {
        dDragonService.updateChampions();
    }

    @Test
    public void update_Items_Test() throws IOException {
        dDragonService.updateItems();
    }

    @Test
    public void update_Spells_Test() throws IOException {
        //System.out.println(dDragonApiCaller.callListApi("summoner").get("data").fieldNames().next());
        dDragonService.updateSpells();
    }

    @Test
    public void update_Runes_Test() throws IOException {
        dDragonService.updateRunes();
    }
}
