package com.nwerl.lolstats.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restConfig(RestTemplateBuilder restTemplateBuilder,
                                   @Value("${apikey}") String apiKey,
                                   @Value("${riotgames.protocol}") String scheme,
                                   @Value("${riotgames.hostname}") String hostname) {
        RestTemplate restTemplate = restTemplateBuilder.requestFactory(()->new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(20000))
                .setReadTimeout(Duration.ofMillis(20000))
                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                .build();

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory
                (UriComponentsBuilder.newInstance()
                .scheme(scheme).host(hostname).path("lol").queryParam("api_key", apiKey)));

        return restTemplate;
    }
}