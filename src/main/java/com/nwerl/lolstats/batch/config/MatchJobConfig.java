package com.nwerl.lolstats.batch.config;

import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
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
public class MatchJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step matchStep(ItemReader<RiotMatchDto> matchReader,
                          ItemProcessor<RiotMatchDto, Match> matchProcessor,
                          ItemWriter<Match> matchWriter) {
        return stepBuilderFactory.get("matchStep")
                .<RiotMatchDto, Match>chunk(1)
                .reader(matchReader)
                .processor(matchProcessor)
                .writer(matchWriter)
                .build();
    }
    @Bean
    public Job matchJob(@Qualifier("matchStep") Step matchStep) {
        return jobBuilderFactory.get("matchJob")
                .incrementer(new RunIdIncrementer())
                .start(matchStep)
                .build();
    }
}