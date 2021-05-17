package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.batch.BatchApplication;
import com.nwerl.lolstats.service.match.MatchApiCaller;
import com.nwerl.lolstats.web.domain.match.MatchReferenceRepository;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.QueueType;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchListDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@MockBean(BatchApplication.class)
@SpringBootTest(classes = {MatchJobTestConfiguration.class})
public class MatchJobTest {
    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;
    @MockBean
    MatchApiCaller matchApiCaller;
    @Autowired
    MatchRepository matchRepository;
    @Autowired
    MatchReferenceRepository matchReferenceRepository;
    @Autowired
    SummonerRepository summonerRepository;

    public static final int PLAYER_NUMBER = 10;

    @Test
    public void matchJob_Integration_Test() throws Exception {
        //given
        String accountId = "abc";
        Long gameId = 123L;
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .addString("accountId", accountId)
                .toJobParameters();


        List<RiotMatchReferenceDto> matchReferenceDtoList = Collections.singletonList(RiotMatchReferenceDto.builder()
                .gameId(gameId).accountId(accountId).queue(QueueType.SOLO_RANK.getQueueCode()).build());
        List<RiotMatchDto.ParticipantIdentityDto> participantIdentities = new ArrayList<>();
        List<RiotMatchDto.ParticipantDto> participantDtos = new ArrayList<>();
        for(int i=1;i<=PLAYER_NUMBER;i++) {
            //MatchWriter에서 Summoner 테이블에 저장된 Player에 한해 MatchReference 테이블에 저장하기 때문에 미리 Summoner Row 저장해 둠.
            summonerRepository.save(Summoner.builder().accountId(Integer.toString(i)).name(Integer.toString(i)).build());

            RiotMatchDto.ParticipantIdentityDto.PlayerDto playerDto = RiotMatchDto.ParticipantIdentityDto.PlayerDto.builder().accountId(Integer.toString(i)).build();
            participantIdentities.add(RiotMatchDto.ParticipantIdentityDto.builder().player(playerDto).build());
            participantDtos.add(RiotMatchDto.ParticipantDto.builder()
                    .runes(Collections.emptyList())
                    .masteries(Collections.emptyList())
                    .stats(RiotMatchDto.ParticipantDto.ParticipantStatsDto.builder().build())
                    .timeline(RiotMatchDto.ParticipantDto.ParticipantTimelineDto.builder().build())
                    .build());
        }

        RiotMatchDto riotMatchDto = RiotMatchDto.builder().gameId(gameId).queueId(QueueType.SOLO_RANK.getQueueCode())
                .participantIdentities(participantIdentities).participants(participantDtos).build();

        given(matchApiCaller.fetchMatchListFromRiotApi(accountId))
                .willReturn(RiotMatchListDto.builder().matches(matchReferenceDtoList).build());
        given(matchApiCaller.fetchMatchFromRiotApi(gameId))
                .willReturn(riotMatchDto);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        assertThat(jobExecution.getExitStatus(), is(ExitStatus.COMPLETED));
        assertThat(matchRepository.findById(gameId).get().getId(), is(gameId));
        for(int i=1;i<=PLAYER_NUMBER;i++) {
            assertThat(matchReferenceRepository.findAllBySummoner_AccountId(Integer.toString(i), PageRequest.of(0, 1)).getContent().get(0).getSummoner().getAccountId(),
                    is(Integer.toString(i)));
        }
    }
}
