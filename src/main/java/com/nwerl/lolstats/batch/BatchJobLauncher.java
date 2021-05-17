package com.nwerl.lolstats.batch;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
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



    @Retryable(value = {IllegalStateException.class}, backoff=@Backoff(delay=100))
    @CacheEvict(value = "ranking")
    public void leagueLaunch() {
        log.info("Job Started at : {}", new Date());

        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        //JobExecution에서 Exception 발생 시 Retryable 통해 3번 반복 후 Unchecked Exception으로 변환 후 Throw
        try {
            JobExecution execution = jobLauncher.run(leagueJob, param);
        } catch(JobExecutionException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    @Retryable(value = {IllegalStateException.class}, backoff=@Backoff(delay=100))
    public void matchLaunch(String accountId) {
        log.info("Job Started at : {}", new Date());

        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .addString("accountId", accountId)
                .toJobParameters();

        try {
            JobExecution execution = jobLauncher.run(matchJob, param);
        }
        catch(JobExecutionException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}
