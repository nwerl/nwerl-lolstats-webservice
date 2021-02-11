package com.nwerl.lolstats.web.domain.match;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class MatchListRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MatchListRepository matchListRepository;

    @Test
    public void findBySummonerId_Test() {
        //참새크면비둘기
        String summonerId = "VtILZ-B2KsRVZeBsSSRpW2dGW3l8wMNY_Lk2G0TiIDqoNgdokyGQJFkp";
        System.out.println(matchListRepository.findByAccountId(summonerId,0, 2).getMatchReferences().size());
    }

    @Test
    public void mongoTemplate_Test() {
        //given
        int prevSize = matchListRepository.findByAccountId("BNHbH704a7gx2MANguAPq64KNErxxmFhic9ti-PHGUxS8Lk")
                .getMatchReferences().size();
        String accountId = "BNHbH704a7gx2MANguAPq64KNErxxmFhic9ti-PHGUxS8Lk";
        MatchReference input = MatchReference.builder().gameId(888L).timeStamp(1234L).build();
        Query query = new Query(Criteria.where("_id").is(accountId));
        Update update = new Update().addToSet("matchReferences", input);

        //when
        mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().upsert(true), MatchList.class);

        //then
        assertThat(prevSize+1, is(matchListRepository.findByAccountId("BNHbH704a7gx2MANguAPq64KNErxxmFhic9ti-PHGUxS8Lk")
                .getMatchReferences().size()));

        mongoTemplate.updateMulti(query,
                new Update().pull("matchReferences", Query.query(Criteria.where("_id").is(888L))),
                MatchList.class);
    }
}