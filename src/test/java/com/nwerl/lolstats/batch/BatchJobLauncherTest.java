package com.nwerl.lolstats.batch;


import com.nwerl.lolstats.config.CacheConfig;
import com.nwerl.lolstats.config.EmbeddedRedisConfig;
import com.nwerl.lolstats.config.RedisConfig;
import com.nwerl.lolstats.service.league.LeagueApiCaller;
import com.nwerl.lolstats.service.league.LeagueService;
import com.nwerl.lolstats.service.slack.SlackSender;
import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.league.LeagueItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.PageImpl;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@EnableCaching
@EnableRetry
@RunWith(SpringRunner.class)
@MockBean(BatchApplication.class)
@SpringBootTest(classes = {BatchJobLauncher.class, RedisConfig.class, EmbeddedRedisConfig.class, CacheConfig.class, LeagueService.class})
public class BatchJobLauncherTest {
    public static final int RETRY_LIMIT = 3;

    @Autowired
    BatchJobLauncher batchJobLauncher;
    @Autowired
    LeagueService leagueService;
    @Autowired
    CacheManager cacheManager;

    @MockBean
    LeagueItemRepository leagueItemRepository;
    @MockBean
    LeagueApiCaller leagueApiCaller;
    @MockBean
    Job job;
    @MockBean
    JobLauncher jobLauncher;
    @MockBean
    SlackSender slackSender;

    @Test
    public void jobLaunch_Retry_Test() throws Exception{
        given(jobLauncher.run(any(), any()))
                .willThrow(IllegalStateException.class)
                .willThrow(IllegalStateException.class)
                .willReturn(eq(any()));

        //when
        batchJobLauncher.leagueLaunch();

        //then
        verify(jobLauncher, times(RETRY_LIMIT)).run(any(), any());
    }

    @Test
    public void cacheEvict는_메소드가_정상적으로_리턴되지_않을시_실행되지_않는다() throws Exception {
        //given
        LeagueItem leagueItem = LeagueItem.builder().summonerName("123").leaguePoints(1557).build();
        PageImpl<LeagueItem> leagueItemsPage = new PageImpl<LeagueItem>(Collections.singletonList(leagueItem));

        given(leagueItemRepository.findAllByOrderByLeaguePointsDesc(any()))
            .willReturn(leagueItemsPage);

        given(jobLauncher.run(any(), any()))
                .willThrow(IllegalStateException.class)
                .willThrow(IllegalStateException.class)
                .willThrow(IllegalStateException.class);


        //when
        leagueService.getLeagueRanking();
        try {
            batchJobLauncher.leagueLaunch();
            fail();
        } catch(Exception e) { }
        leagueService.getLeagueRanking();


        //then
        verify(leagueItemRepository, times(1)).findAllByOrderByLeaguePointsDesc(any());
    }
}
