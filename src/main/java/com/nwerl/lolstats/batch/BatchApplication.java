package com.nwerl.lolstats.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Component
public class BatchApplication {
    private final JobLauncher jobLauncher;
    private final Job matchJob;
    private final Job leagueJob;
    private final LeagueItemMap leagueItemMap;


    @Autowired
    public BatchApplication(JobLauncher jobLauncher,
                            @Qualifier("matchJob") Job matchJob,
                            @Qualifier("leagueJob") Job leagueJob,
                            LeagueItemMap leagueItemMap) {
        this.jobLauncher = jobLauncher;
        this.matchJob = matchJob;
        this.leagueJob = leagueJob;
        this.leagueItemMap = leagueItemMap;
    }


    @Scheduled (fixedDelay= Long.MAX_VALUE)
    public void initLeagueList() throws Exception {
        leagueLaunch();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void leagueLaunch() throws Exception {
        log.info("Job Started at : {}", new Date());

        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution execution = jobLauncher.run(leagueJob, param);

        leagueItemMap.update();
    }

    @CacheEvict(value = "match", key = "#summonerName")
    public void matchLaunch(String summonerName) throws Exception {
        log.info("Job Started at : {}", new Date());

        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .addString("accountId", leagueItemMap.getAccountId(summonerName))
                .toJobParameters();
        JobExecution execution = jobLauncher.run(matchJob, param);
    }

    @Scheduled(initialDelay = 3000, fixedDelay = 100)
    public void matchJobExecute() throws Exception {
        String summonerName = leagueItemMap.getNextSummonerName();
        matchLaunch(summonerName);
    }
}
