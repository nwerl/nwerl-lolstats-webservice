package com.nwerl.lolstats.web.domain.match;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
public class MatchListRepositoryTest {
    @Autowired
    private MatchListRepository matchListRepository;

    @Test
    public void findBySummonerName_Test() {
        String summonerName = "JUGKlNG";
        System.out.println(matchListRepository.findBySummonerName(summonerName,0, 20).getMatchReferences().get(0).getGameId());
    }

//    @Test
//    public void matchListWriter_Test(){
//        String accountId = "CuufRXm4Cm2ITWbgem3fbGGXCdAhSFsPCEPF-5wxEpzPVYw";
//        Integer prevSize = matchListRepository.findByAccountId(accountId).getMatchReferences().size();
//
//        List<MatchReference> list = new ArrayList<>();
//        list.add(MatchReference.builder().gameId(123456L).build());
//
//        MatchList matchList = matchListRepository.findByAccountId(accountId);
//        matchList.addMatchReferences(list);
//
//        System.out.println(matchListRepository.findByAccountId(accountId).getMatchReferences().size());
//
//        //assertThat(prevSize+1, is(matchListRepository.findByAccountId(accountId).getMatchReferences().size()+1));
//    }
}