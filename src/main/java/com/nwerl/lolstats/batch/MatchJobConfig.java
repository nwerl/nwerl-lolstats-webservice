package com.nwerl.lolstats.batch;

import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.domain.match.MatchReference;
import com.nwerl.lolstats.web.dto.riotApi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.RiotMatchReferenceDto;
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
    public ExecutionContextPromotionListener promotionListener () {
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
        // 데이터 공유를 위해 사용될 key값을 미리 빈에 등록해주어야 합니다.
        executionContextPromotionListener.setKeys(new String[]{"GAME_ID"});

        return executionContextPromotionListener;
    }

    @Bean
    public Step matchStep1(ItemReader<RiotMatchReferenceDto> matchListReader,
                           ItemProcessor<RiotMatchReferenceDto, MatchReference> matchListProcessor,
                           ItemWriter<MatchReference> matchListWriter) {
        return stepBuilderFactory.get("matchStep1")
                .<RiotMatchReferenceDto, MatchReference>chunk(1)
                .reader(matchListReader)
                .processor(matchListProcessor)
                .writer(matchListWriter)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step matchStep2(ItemReader<RiotMatchDto> matchReader,
                           ItemProcessor<RiotMatchDto, Match> matchProcessor,
                           ItemWriter<Match> matchWriter) {
        return stepBuilderFactory.get("matchStep2")
                .<RiotMatchDto, Match>chunk(1)
                .reader(matchReader)
                .processor(matchProcessor)
                .writer(matchWriter)
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Job matchJob(@Qualifier("matchStep1") Step matchStep1,
                        @Qualifier("matchStep2") Step matchStep2) {
        return jobBuilderFactory.get("matchJob")
                .incrementer(new RunIdIncrementer())
                .start(matchStep1).on("FAILED").end()
                .from(matchStep1).on("*").to(matchStep2)
                .end()
                .build();
    }
}
