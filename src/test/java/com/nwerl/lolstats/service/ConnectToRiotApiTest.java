package com.nwerl.lolstats.service;

import com.nwerl.lolstats.batch.BatchApplication;
import com.nwerl.lolstats.service.datadragon.DataDragonApiCaller;
import com.nwerl.lolstats.service.datadragon.DataDragonService;
import com.nwerl.lolstats.service.league.LeagueApiCaller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@MockBean(BatchApplication.class)
@SpringBootTest
public class ConnectToRiotApiTest {
    @Autowired
    private LeagueApiCaller leagueApiCaller;
    @Autowired
    private DataDragonService dataDragonService;
    @Autowired
    private DataDragonApiCaller dataDragonApiCaller;

    @Test
    public void connect_to_Riot_Api() {
        leagueApiCaller.fetchChallengerLeagueListFromRiotApi();
    }

    @Test
    public void update_Version_Test() {
        String version = "11.4.1";

        assertThat(version, is(dataDragonApiCaller.callApiCurrentLOLVersion()));
    }

    @Test
    public void update_Champions_Test() throws IOException {
        dataDragonService.updateChampions();
    }

    @Test
    public void update_Items_Test() throws IOException {
        dataDragonService.updateItems();
    }

    @Test
    public void update_Spells_Test() throws IOException {
        dataDragonService.updateSpells();
    }

    @Test
    public void update_RuneStyles_Test() throws IOException {
        dataDragonService.updateRuneStyles();
    }

    @Test
    public void update_Runes_Test() throws IOException {
        dataDragonService.updateRunes();
    }

    @Test
    public void update_All_Images() throws IOException {
        update_Champions_Test();
        update_Items_Test();
        update_Runes_Test();
        update_RuneStyles_Test();
        update_Spells_Test();
    }
}
