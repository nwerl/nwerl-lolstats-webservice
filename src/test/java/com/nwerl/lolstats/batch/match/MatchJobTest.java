package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.batch.BatchApplication;
import com.nwerl.lolstats.batch.MatchIdSet;
import com.nwerl.lolstats.batch.MatchJobConfig;
import com.nwerl.lolstats.service.league.LeagueService;
import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.service.summoner.SummonerService;
import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.domain.match.MatchListRepository;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueListDto;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
    private MatchIdSet matchIdSet;
    @Autowired
    private MatchJobConfig matchJobConfig;
    @Autowired
    private MatchRepository matchRepository;
    @MockBean
    private MatchService matchService;
    @MockBean
    private LeagueService leagueService;
    @MockBean
    private SummonerService summonerService;
    @MockBean
    private MatchListRepository matchListRepository;
    @MockBean
    private ItemProcessor<RiotMatchDto, Match> itemProcessor;

    @Test
    public void matchJob_Integration_Test() throws Exception {
        //given
        List<String> accountIdList = Arrays.asList("123", "456", "789", "999");
        when(leagueService.findAll()).thenReturn(RiotLeagueListDto.builder().entries(Collections.emptyList()).build());
        when(summonerService.findAccountIdListByIdList(any())).thenReturn(accountIdList);

        for(String accountId : accountIdList)
        {
            Long gameId = Long.parseLong(accountId);
            RiotMatchDto riotMatchDto = RiotMatchDto.builder().gameId(gameId).build();
            when(matchService.fetchLastMatchReferenceFromRiotApi(accountId)).thenReturn(RiotMatchReferenceDto.builder().gameId(gameId).timestamp(gameId).build());
            when(matchService.fetchMatchFromRiotApi(gameId)).thenReturn(riotMatchDto);
            when(itemProcessor.process(riotMatchDto)).thenReturn(Match.builder().gameId(gameId).build());
        }

        when(matchService.existsByGameId(123L)).thenReturn(false);
        when(matchService.existsByGameId(456L)).thenReturn(true);
        when(matchService.existsByGameId(789L)).thenReturn(false);
        when(matchService.existsByGameId(999L)).thenReturn(true);

        //then
        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(param);

        //then
        assertThat(matchRepository.existsByGameId(123L), is(true));
        matchRepository.deleteById(123L);
        assertThat(matchRepository.existsByGameId(456L), is(false));
        matchRepository.deleteById(456L);
        assertThat(matchRepository.existsByGameId(789L), is(true));
        matchRepository.deleteById(789L);
        assertThat(matchRepository.existsByGameId(999L), is(false));
    }
}
