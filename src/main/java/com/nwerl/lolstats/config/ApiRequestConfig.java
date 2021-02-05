package com.nwerl.lolstats.config;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.time.Duration;

@Configuration
public class ApiRequestConfig {
    @Value("${apikey}")
    private String apiKey;

    @Bean
    public UriComponentsBuilder uriComponentsBuilderConfig() {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("kr.api.riotgames.com/lol").queryParam("api_key", apiKey);
    }

    @Bean
    public RestTemplate restTemplateConfig(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.requestFactory(()->new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(20000))
                .setReadTimeout(Duration.ofMillis(20000))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .build();
    }
}