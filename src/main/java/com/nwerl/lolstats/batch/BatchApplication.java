package com.nwerl.lolstats.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class BatchApplication {
    private final BatchJobLauncher batchJobLauncher;
    private final LeagueItemMap leagueItemMap;

    @Scheduled(fixedDelay = Long.MAX_VALUE)
    public void initLeagueList() throws Exception {
        batchJobLauncher.leagueLaunch();
        leagueItemMap.update();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void leagueJobExecute() throws Exception {
        batchJobLauncher.leagueLaunch();
    }

    @Scheduled(initialDelay = 3000, fixedDelay = 100)
    public void matchJobExecute() throws Exception {
        String summonerName = leagueItemMap.getNextSummonerName();
        batchJobLauncher.matchLaunch(summonerName, leagueItemMap.getAccountId(summonerName));
    }
}
