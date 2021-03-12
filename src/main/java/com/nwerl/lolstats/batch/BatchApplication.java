package com.nwerl.lolstats.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Component
public class BatchApplication {
    private final JobLauncher jobLauncher;
    private final Job matchJob;
    private final Job leagueJob;
    private final LeagueItemAccountIdList leagueItemAccountIdList;

    @Autowired
    public BatchApplication(JobLauncher jobLauncher,
                            LeagueItemAccountIdList leagueItemAccountIdList,
                            @Qualifier("matchJob") Job matchJob,
                            @Qualifier("leagueJob") Job leagueJob) {
        this.jobLauncher = jobLauncher;
        this.leagueItemAccountIdList = leagueItemAccountIdList;
        this.matchJob = matchJob;
        this.leagueJob = leagueJob;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void leagueLaunch() throws Exception {
        log.info("Job Started at : {}", new Date());

        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution execution = jobLauncher.run(leagueJob, param);

        leagueItemAccountIdList.update();
    }

    @Scheduled(fixedDelay = 100)
    public void matchLaunch() throws Exception {
        log.info("Job Started at : {}", new Date());

        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution execution = jobLauncher.run(matchJob, param);
    }
}
