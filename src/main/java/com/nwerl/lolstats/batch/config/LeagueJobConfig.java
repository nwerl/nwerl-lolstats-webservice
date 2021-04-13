package com.nwerl.lolstats.batch.config;

import com.nwerl.lolstats.batch.league.LeagueListRemoverTasklet;
import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import com.nwerl.lolstats.web.dto.riotapi.summoner.RiotSummonerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class LeagueJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final int CHALLENGER_SUMMONER_NUMBER = 300;

    @Bean
    public Step removeOldLeagueListStep(LeagueListRemoverTasklet leagueListRemoverTasklet) {
        return stepBuilderFactory.get("removeOldLeagueListStep")
                .tasklet(leagueListRemoverTasklet)
                .build();
    }

    @Bean
    public Step leagueListStep(ItemReader<RiotLeagueItemDto> leagueReader,
                               ItemProcessor<RiotLeagueItemDto, LeagueItem> leagueProcessor,
                               ItemWriter<LeagueItem>  leagueWriter) {
        return stepBuilderFactory.get("leagueListReader")
                .<RiotLeagueItemDto, LeagueItem>chunk(CHALLENGER_SUMMONER_NUMBER)
                .reader(leagueReader)
                .processor(leagueProcessor)
                .writer(leagueWriter)
                .build();
    }


    @Bean
    public Step summonerStep(ItemReader<RiotSummonerDto> summonerReader,
                            ItemProcessor<RiotSummonerDto, Summoner> summonerProcessor,
                            ItemWriter<Summoner> summonerWriter) {
        return stepBuilderFactory.get("summonerStep")
                .<RiotSummonerDto, Summoner>chunk(CHALLENGER_SUMMONER_NUMBER)
                .reader(summonerReader)
                .processor(summonerProcessor)
                .writer(summonerWriter)
                .build();
    }


    @Bean
    public Job leagueJob(@Qualifier("removeOldLeagueListStep") Step removeOldLeagueListStep,
                         @Qualifier("leagueListStep") Step leagueListStep,
                         @Qualifier("summonerStep") Step summonerStep) {
        return jobBuilderFactory.get("leagueJob")
                .incrementer(new RunIdIncrementer())
                .start(removeOldLeagueListStep)
                .next(leagueListStep).on("FAILED").end()
                .from(leagueListStep).on("*").to(summonerStep)
                .end()
                .build();
    }
}
