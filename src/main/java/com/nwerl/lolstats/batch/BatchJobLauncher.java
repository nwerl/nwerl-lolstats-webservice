package com.nwerl.lolstats.batch;


import com.nwerl.lolstats.service.slack.SlackSender;
import com.nwerl.lolstats.web.dto.slack.SlackMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Component
public class BatchJobLauncher {
    @Qualifier("matchJob")
    private final Job matchJob;
    @Qualifier("leagueJob")
    private final Job leagueJob;
    private final JobLauncher jobLauncher;
    private final SlackSender slackSender;

    private AtomicBoolean isValidRiotApiKey = new AtomicBoolean(true);

    @Retryable(value = {IllegalStateException.class}, backoff=@Backoff(delay=100))
    @CacheEvict(value = "ranking")
    public void leagueLaunch() {
        log.info("Job Started at : {}", LocalDateTime.now());

        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        //JobExecution에서 Exception 발생 시 Retryable 통해 3번 반복 후 Unchecked Exception으로 변환 후 Throw
        try {
            /*JobExecution에서 Exception은 두가지로 나눠진다.
            1. execution context 생성 자체가 실패한 경우.
            - 이 경우는 Checked Exception 발생하기 때문에 따로 처리를 해주어야 한다.
            2. execution context 생성은 성공했으나 step에서 Exception 발생.
            - 이 경우는 리턴받는 execution context에 Exception 기록이 찍힌다.
            - execution context 자체는 성공했기 때문에 Job 내부의 예외를 JobExecution 레벨에서 Try-Catch로 핸들링은 불가능하다.
            - 내부에서 일어난 예외 처리는 1)getAllFailureExceptions를 불러 와서 Exception 처리를 하거나 2)내부에서 예외처리 하거나
             */
            JobExecution execution = jobLauncher.run(leagueJob, param);

            if(needSlackMessage(execution))
                slackSender.send(SlackMessage.alarmRiotApiKey());
        } catch(JobExecutionException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    @Retryable(value = {IllegalStateException.class}, backoff=@Backoff(delay=100))
    public void matchLaunch(String accountId) {
        log.info("Job Started at : {}", LocalDateTime.now());

        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .addString("accountId", accountId)
                .toJobParameters();

        try {
            JobExecution execution = jobLauncher.run(matchJob, param);

            if(needSlackMessage(execution))
                slackSender.send(SlackMessage.alarmRiotApiKey());
        } catch(JobExecutionException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private boolean needSlackMessage(JobExecution execution) {
        if(execution == null)
            return false;

        boolean is401Or403 = execution.getAllFailureExceptions().stream()
                .anyMatch(t -> t.getClass().equals(HttpClientErrorException.Unauthorized.class) ||
                        t.getClass().equals(HttpClientErrorException.Forbidden.class));

        if(is401Or403 && isValidRiotApiKey.get()) {
            isValidRiotApiKey.set(false);
            log.info("Riot API Key is not valid!! Check your API Key!!");
            return true;
        }
        else if(!is401Or403) {
            isValidRiotApiKey.set(true);
        }
        return false;
    }
}