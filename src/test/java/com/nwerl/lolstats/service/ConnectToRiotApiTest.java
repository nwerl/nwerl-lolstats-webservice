package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.dto.riotApi.ddragon.RiotChampionsDto;
import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void connect_to_Riot_Api() {
        String name = "Vehumet";
        SummonerDto summonerDto = summonerService.callApiSummonerInfoByName(name);
        assertThat(name, is(summonerDto.getName()));
    }

    @Test
    public void get_Current_Version_Test() {
        System.out.println(dDragonService.callApiCurrentLOLVersion());
    }

    @Test
    public void get_Champions_Data_Test() {
        System.out.println(dDragonService.callApiChampionList());
    }

    @Test
    public void get_Champion_ThumbNail_Test() {
        String name = "Aatrox";
        System.out.println(Arrays.toString(dDragonService.callApiChampionThumbNail(name)));
    }

    @Test
    public void update_Champions_Test() {
        dDragonService.updateChampions();
    }
}
