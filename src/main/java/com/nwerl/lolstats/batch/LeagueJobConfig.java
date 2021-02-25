package com.nwerl.lolstats.batch;

import com.nwerl.lolstats.batch.summoner.SummonerListener;
import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
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
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class LeagueJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step leagueStep1(ItemReader<List<LeagueItemDto>> leagueItemReader,
                            ItemProcessor<List<LeagueItemDto>, List<LeagueItem>> leagueItemProcessor,
                            ItemWriter<List<LeagueItem>> leagueItemWriter) {
        return stepBuilderFactory.get("leagueStep1")
                .<List<LeagueItemDto>, List<LeagueItem>>chunk(1)
                .reader(leagueItemReader)
                .processor(leagueItemProcessor)
                .writer(leagueItemWriter)
                .faultTolerant()
                .retryLimit(3)
                .retry(HttpServerErrorException.class)
                .build();
    }

    @Bean
    public Step leagueStep2(ItemReader<SummonerDto> summonerReader,
                            ItemProcessor<SummonerDto, Summoner> summonerProcessor,
                            ItemWriter<Summoner> summonerWriter) {
        return stepBuilderFactory.get("leagueStep2")
                .<SummonerDto, Summoner>chunk(1)
                .reader(summonerReader)
                .processor(summonerProcessor)
                .writer(summonerWriter)
                .listener(new SummonerListener())
                .build();
    }


    @Bean
    public Job leagueJob(@Qualifier("leagueStep1") Step leagueStep1,
                         @Qualifier("leagueStep2") Step leagueStep2) {
        return jobBuilderFactory.get("leagueJob")
                .incrementer(new RunIdIncrementer())
                .start(leagueStep1).on("FAILED").end()
                .from(leagueStep1).on("*").to(leagueStep2)
                .end()
                .build();
    }
}
