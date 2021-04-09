package com.nwerl.lolstats.batch;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class BatchJobLauncher {
    @Qualifier("matchJob")
    private final Job matchJob;
    @Qualifier("leagueJob")
    private final Job leagueJob;
    private final JobLauncher jobLauncher;


    @CacheEvict(value = "ranking")
    public void leagueLaunch() throws Exception {
        log.info("Job Started at : {}", new Date());

        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution execution = jobLauncher.run(leagueJob, param);
    }

    public void matchLaunch(String summonerName, String accountId) throws Exception {
        log.info("Job Started at : {}", new Date());

        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .addString("accountId", accountId)
                .toJobParameters();
        JobExecution execution = jobLauncher.run(matchJob, param);
    }
}
