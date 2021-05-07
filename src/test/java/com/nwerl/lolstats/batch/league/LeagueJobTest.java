package com.nwerl.lolstats.batch.league;

import com.nwerl.lolstats.batch.BatchApplication;
import com.nwerl.lolstats.service.league.LeagueApiCaller;
import com.nwerl.lolstats.service.summoner.SummonerApiCaller;
import com.nwerl.lolstats.web.domain.league.LeagueItemRepository;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueListDto;
import com.nwerl.lolstats.web.dto.riotapi.summoner.RiotSummonerDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@MockBean(BatchApplication.class)
@SpringBootTest(classes = {LeagueJobTestConfiguration.class})
public class LeagueJobTest {
    @Autowired
    @Qualifier("leagueJobUtils")
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private LeagueItemRepository leagueItemRepository;
    @Autowired
    private SummonerRepository summonerRepository;
    @MockBean
    private LeagueApiCaller leagueApiCaller;
    @MockBean
    private SummonerApiCaller summonerApiCaller;

    public final static int TEST_DATA_LIMIT = 10;

    @Test
    public void leagueJob_Integeration_Test() throws Exception {
        //given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        RiotLeagueListDto riotLeagueListDto = RiotLeagueListDto.builder().entries(getRiotLeagueItems()).build();
        given(leagueApiCaller.fetchChallengerLeagueListFromRiotApi()).willReturn(riotLeagueListDto);
        for(int i=1;i<=TEST_DATA_LIMIT;i++) {
            RiotSummonerDto riotSummonerDto = getRiotSummonerDtosById(i);
            given(summonerApiCaller.fetchSummonerFromRiotApiById(Integer.toString(i))).willReturn(riotSummonerDto);
        }

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        assertThat(jobExecution.getExitStatus(), is(ExitStatus.COMPLETED));
        for(int i=1;i<=TEST_DATA_LIMIT;i++) {
            assertThat(leagueItemRepository.findById((long) i).get().getSummonerId(), is(Integer.toString(i)));
            assertThat(summonerRepository.findById((long) i).get().getSummonerId(), is(Integer.toString(i)));
        }
    }

    private RiotSummonerDto getRiotSummonerDtosById(int id) {
        return RiotSummonerDto.builder()
                .id(Integer.toString(id))
                .accountId(Integer.toString(id))
                .name(Character.toChars(id).toString())
                .build();
    }


    private List<RiotLeagueItemDto> getRiotLeagueItems() {
        List<RiotLeagueItemDto> riotLeagueItems = new LinkedList<>();

        for(int i=1;i<=TEST_DATA_LIMIT;i++) {
            riotLeagueItems.add(RiotLeagueItemDto.builder()
                    .summonerId(Integer.toString(i))
                    .summonerName(Character.toChars(i).toString())
                    .leaguePoints(i)
                    .build()
            );
        }

        return riotLeagueItems;
    }
}
