package com.nwerl.lolstats.batch;

import com.nwerl.lolstats.batch.summoner.SummonerListener;
import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.domain.match.MatchReference;
import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import com.nwerl.lolstats.web.dto.riotApi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.RiotMatchReferenceDto;
import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ItemReader<RiotMatchReferenceDto> matchListReader;
    private final ItemProcessor<RiotMatchReferenceDto, MatchReference> matchListProcessor;
    private final ItemWriter<MatchReference> matchListWriter;

    private final ItemReader<RiotMatchDto> matchReader;
    private final ItemProcessor<RiotMatchDto, Match> matchProcessor;
    private final ItemWriter<Match> matchWriter;

    private final ItemReader<List<LeagueItemDto>> leagueItemReader;
    private final ItemProcessor<List<LeagueItemDto>, List<LeagueItem>> leagueItemProcessor;
    private final ItemWriter<List<LeagueItem>> leagueItemWriter;

    private final ItemReader<SummonerDto> summonerReader;
    private final ItemProcessor<SummonerDto, Summoner> summonerProcessor;
    private final ItemWriter<Summoner> summonerWriter;

    @Bean
    public ExecutionContextPromotionListener promotionListener () {
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
        // 데이터 공유를 위해 사용될 key값을 미리 빈에 등록해주어야 합니다.
        executionContextPromotionListener.setKeys(new String[]{"GAME_ID"});

        return executionContextPromotionListener;
    }

    @Bean
    public Step matchStep1() {
        return stepBuilderFactory.get("matchStep1")
                .<RiotMatchReferenceDto, MatchReference>chunk(1)
                .reader(matchListReader)
                .processor(matchListProcessor)
                .writer(matchListWriter)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step matchStep2() {
        return stepBuilderFactory.get("matchStep2")
                .<RiotMatchDto, Match>chunk(1)
                .reader(matchReader)
                .processor(matchProcessor)
                .writer(matchWriter)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Job matchJob() {
        return jobBuilderFactory.get("matchJob")
                .incrementer(new RunIdIncrementer())
                .start(matchStep1()).on("FAILED").end()
                .from(matchStep1()).on("*").to(matchStep2())
                .end()
                .build();
    }
    @Bean
    public Step leagueStep1() {
        return stepBuilderFactory.get("leagueStep1")
                .<List<LeagueItemDto>, List<LeagueItem>>chunk(1)
                .reader(leagueItemReader)
                .processor(leagueItemProcessor)
                .writer(leagueItemWriter)
                .build();
    }

    @Bean
    public Step leagueStep2() {
        return stepBuilderFactory.get("leagueStep2")
                .<SummonerDto, Summoner>chunk(1)
                .reader(summonerReader)
                .processor(summonerProcessor)
                .writer(summonerWriter)
                .listener(new SummonerListener())
                .build();
    }


    @Bean
    public Job leagueJob () {
        return jobBuilderFactory.get("leagueJob")
                .incrementer(new RunIdIncrementer())
                .start(leagueStep1()).on("FAILED").end()
                .from(leagueStep1()).on("*").to(leagueStep2())
                .end()
                .build();
    }
}
