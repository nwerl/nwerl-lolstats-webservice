package com.nwerl.lolstats.batch;

import com.nwerl.lolstats.service.league.LeagueApiCaller;
import com.nwerl.lolstats.service.league.LeagueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@MockBean(BatchApplication.class)
@EnableAutoConfiguration
@SpringBootTest(classes = {BatchJobTestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class LeagueJobTest {
    @Autowired
    @Qualifier("leagueJob")
    private Job job;

    @Autowired
    @Qualifier("leagueJobUtils")
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private LeagueApiCaller leagueApiCaller;

    @Test
    public void leagueJob_Retry_Test() throws Exception {
        JobLauncher jobLauncher = jobLauncherTestUtils.getJobLauncher();
        given(leagueApiCaller.callApiChallengerLeagueItem()).willThrow(HttpServerErrorException.class);
        JobExecution jobExecution = jobLauncher.run(job, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
        ExitStatus exitStatus = jobExecution.getExitStatus();
        assertThat(exitStatus.getExitCode(), is("COMPLETED"));
    }
}
