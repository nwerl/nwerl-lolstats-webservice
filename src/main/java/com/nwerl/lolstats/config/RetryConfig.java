package com.nwerl.lolstats.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableRetry
@Configuration
public class RetryConfig {
    private static final Long RIOT_API_TIMEOUT = 120000L;
    private static final Long ADDITIONAL_TIME = 20000L;
    private static final int MAX_ATTEMPTS = 2;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(RIOT_API_TIMEOUT+ADDITIONAL_TIME);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        retryTemplate.registerListener(new RetryListenerSupport() {
            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                log.info("{} / RetryCount : {}",
                        throwable.toString(), context.getRetryCount());
            }
        });

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_ATTEMPTS);
        ExceptionClassifierRetryPolicy classifierRetryPolicy = new ExceptionClassifierRetryPolicy();
        Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();
        policyMap.put(HttpClientErrorException.TooManyRequests.class, retryPolicy);
        classifierRetryPolicy.setPolicyMap(policyMap);

        retryTemplate.setRetryPolicy(classifierRetryPolicy);

        return retryTemplate;
    }
}