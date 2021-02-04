package com.nwerl.lolstats.batch.summoner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
public class SummonerListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
