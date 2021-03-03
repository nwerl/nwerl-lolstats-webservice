package com.nwerl.lolstats.batch.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
public class ItemFailureListener<I, O> extends ItemListenerSupport<I, O> {
    private static final int riotApiCallLimit = 120000 + 20000;

    @SneakyThrows
    @Override
    public void onReadError(Exception ex) {
        if(ex instanceof HttpClientErrorException.TooManyRequests) {
            log.info("429 Exception... wait for 2 minutes...");
            Thread.sleep(riotApiCallLimit);
        }
    }

    public ItemProcessListener<I, O> asItemProcessListener() {
        return this;
    }
}
