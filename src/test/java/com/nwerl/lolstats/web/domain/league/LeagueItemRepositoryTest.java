package com.nwerl.lolstats.web.domain.league;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class LeagueItemRepositoryTest {
    @Autowired
    private LeagueItemRepository leagueItemRepository;

    @Test
    public void findAllOrderByLeaguePointsDesc_Test() {
        List<LeagueItem> list = new ArrayList<>();
        list.add(LeagueItem.builder().summonerId("123").leaguePoints(11111).build());
        list.add(LeagueItem.builder().summonerId("456").leaguePoints(99999).build());
        list.add(LeagueItem.builder().summonerId("789").leaguePoints(55555).build());
        list.add(LeagueItem.builder().summonerId("999").leaguePoints(99999).build());
        leagueItemRepository.saveAll(list);

        list = leagueItemRepository.findAllByOrderByLeaguePointsDesc();
        LeagueItem first = list.get(0);
        LeagueItem second = list.get(1);
        LeagueItem third = list.get(2);

        assertThat(first.getSummonerId(), is("456"));
        assertThat(second.getSummonerId(), is("999"));
        assertThat(third.getSummonerId(), is("789"));
    }
}
