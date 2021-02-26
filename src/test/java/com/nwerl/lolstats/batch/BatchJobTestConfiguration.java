package com.nwerl.lolstats.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableBatchProcessing
public class BatchJobTestConfiguration {
    @Bean
    @Qualifier("leagueJob")
    JobLauncherTestUtils leagueJobUtils() {
        return new JobLauncherTestUtils(){
            @Override
            @Autowired
            public void setJob(@Qualifier("leagueJob") Job job) {
                super.setJob(job);
            }
        };
    }
}
