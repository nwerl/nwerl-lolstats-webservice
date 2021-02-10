package com.nwerl.lolstats.batch;

import com.nwerl.lolstats.service.LeagueService;
import com.nwerl.lolstats.service.SummonerService;
import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class BatchApplication {
    private final JobLauncher jobLauncher;
    private final Job matchJob;
    private final Job leagueJob;

    private final LeagueService leagueService;
    private final SummonerService summonerService;

    private LeagueListDto leagueList;
    private Map<String, String> accountIdMap;

    @Autowired
    public BatchApplication(JobLauncher jobLauncher,
                            LeagueService leagueService,
                            SummonerService summonerService,
                            @Qualifier("matchJob") Job matchJob,
                            @Qualifier("leagueJob") Job leagueJob) {
        this.jobLauncher = jobLauncher;
        this.leagueService = leagueService;
        this.matchJob = matchJob;
        this.leagueJob = leagueJob;
        this.summonerService = summonerService;
        accountIdMap = new HashMap<>();
    }

    @Scheduled(fixedDelay = 3600000)
    public void leagueLaunch() throws Exception {
        log.info("Job Started at : {}", new Date());
        JobParameters param = new JobParametersBuilder()
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution execution = jobLauncher.run(leagueJob, param);

        leagueList = this.leagueService.findAll();
        for(LeagueItemDto item : leagueList.getEntries()) {
            accountIdMap.put(item.getSummonerName(), summonerService.findAccountIdById(item.getSummonerId()));
        }
    }


    @Scheduled(fixedRateString = "3000", initialDelay = 5000)
    public void matchLaunch() throws Exception {
        log.info("Job Started at : {}", new Date());

        JobParameters param = new JobParametersBuilder()
                .addString("accountId",  accountIdMap.get(leagueList.traversal().getSummonerName()))
                .addString("summonerName", leagueList.traversal().getSummonerName())
                .addString("dateTime", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution execution = jobLauncher.run(matchJob, param);
    }
}
