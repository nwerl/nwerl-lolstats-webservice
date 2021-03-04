package com.nwerl.lolstats.batch.config;

import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.domain.match.MatchList;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
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
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MatchJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step matchListStep(ItemReader<RiotMatchReferenceDto> matchListReader,
                              ItemProcessor<RiotMatchReferenceDto, MatchList> matchListProcessor,
                              ItemWriter<MatchList> matchListWriter) {
        return stepBuilderFactory.get("matchListStep")
                .<RiotMatchReferenceDto, MatchList>chunk(1)
                .reader(matchListReader)
                .processor(matchListProcessor)
                .writer(matchListWriter)
                .listener(new ItemFailureListener<RiotMatchReferenceDto, MatchList>().asItemProcessListener())
                .faultTolerant()
                .noRollback(HttpClientErrorException.TooManyRequests.class)
                .build();
    }

    @Bean
    public Step matchStep(ItemReader<RiotMatchDto> matchReader,
                          ItemProcessor<RiotMatchDto, Match> matchProcessor,
                          ItemWriter<Match> matchWriter) {
        return stepBuilderFactory.get("matchStep")
                .<RiotMatchDto, Match>chunk(1)
                .reader(matchReader)
                .processor(matchProcessor)
                .writer(matchWriter)
                .listener(new ItemFailureListener<RiotMatchDto, Match>().asItemProcessListener())
                .faultTolerant()
                .noRollback(HttpClientErrorException.TooManyRequests.class)
                .build();
    }

    @Bean
    public Job matchJob(@Qualifier("matchListStep") Step matchListStep,
                        @Qualifier("matchStep") Step matchStep) {
        return jobBuilderFactory.get("matchJob")
                .incrementer(new RunIdIncrementer())
                .start(matchListStep).on("FAILED").end()
                .from(matchListStep).on("*").to(matchStep)
                .end()
                .build();
    }
}
