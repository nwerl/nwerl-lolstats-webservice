package com.nwerl.lolstats.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Slf4j
@RequiredArgsConstructor
@Component
public class BatchApplication {
    private final BatchJobLauncher batchJobLauncher;
    private final LeagueItemMap leagueItemMap;

    //PostConstruct에서는 Checked Exception Throw하면 절대 안된다!!
    //PostConstruct는 Bean의 init 작업이기 때문에 실패하면 애플리케이션이 아예 안뜸.
    @PostConstruct
    public void initLeagueList() {
        batchJobLauncher.leagueLaunch();
        leagueItemMap.update();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void leagueJobExecute() {
        batchJobLauncher.leagueLaunch();
    }

    @Scheduled(initialDelay = 3000, fixedDelay = 100)
    public void matchJobExecute() {
        String summonerName = leagueItemMap.getNextSummonerName();
        batchJobLauncher.matchLaunch(leagueItemMap.getAccountId(summonerName));
    }
}
