package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.batch.BatchApplication;
import com.nwerl.lolstats.batch.config.MatchJobConfig;
import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.domain.match.MatchListRepository;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@MockBean(BatchApplication.class)
@EnableAutoConfiguration
@SpringBootTest(classes = {MatchJobTestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MatchJobTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private MatchJobConfig matchJobConfig;
    @Autowired
    private MatchRepository matchRepository;
    @MockBean
    private MatchService matchService;
    @MockBean
    private MatchListRepository matchListRepository;
    @MockBean
    private ItemProcessor<RiotMatchDto, Match> itemProcessor;
    private String accountId;


    @Test
    public void matchJob_Integration_Test() throws Exception {
        //given
        accountId = "123";
        Long gameId = Long.parseLong(accountId);

        RiotMatchDto riotMatchDto = RiotMatchDto.builder().gameId(gameId).build();
        when(matchService.fetchLastRankMatchReferenceFromRiotApi(accountId)).thenReturn(RiotMatchReferenceDto.builder().gameId(gameId).timestamp(gameId).build());
        when(matchService.fetchMatchFromRiotApi(gameId)).thenReturn(riotMatchDto);
        when(itemProcessor.process(riotMatchDto)).thenReturn(Match.builder().gameId(gameId).build());
        when(matchService.existsByGameId(gameId)).thenReturn(false);

        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .addString("accountId", accountId)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(param);

        //then
        assertThat(matchRepository.existsByGameId(gameId), is(true));
    }

    @After
    public void removeTestData() {
        matchRepository.deleteById(Long.parseLong(accountId));
    }
}
