package com.nwerl.lolstats.batch;

import com.nwerl.lolstats.web.domain.match.FeaturedGameInfo;
import com.nwerl.lolstats.web.dto.FeaturedGameInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@EnableBatchProcessing
@Configuration
public class BatchConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobLauncher jobLauncher;
    private final ItemReader<List<FeaturedGameInfoDto>>featuredGameInfoReader;
    private final ItemProcessor<List<FeaturedGameInfoDto>, List<FeaturedGameInfo>> featuredGameInfoProcessor;
    private final ItemWriter<List<FeaturedGameInfo>> featuredGameInfoWriter;

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<List<FeaturedGameInfoDto>, List<FeaturedGameInfo>>chunk(1)
                .reader(featuredGameInfoReader)
                .processor(featuredGameInfoProcessor)
                .writer(featuredGameInfoWriter)
                .build();
    }

    @Bean
    public Job featuredGameInfoJob() {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .listener(new FeaturedGameInfoListener())
                .flow(step1())
                .end()
                .build();
    }

    @Scheduled(fixedDelay = 5000L)
    public void perform() throws Exception {
        log.info("Job Started at : {}", new Date());
        JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution execution = jobLauncher.run(featuredGameInfoJob(), param);

        log.info ("Job finished with status: {}", execution.getStatus());
    }
}
