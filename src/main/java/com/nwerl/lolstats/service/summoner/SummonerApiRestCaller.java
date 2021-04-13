package com.nwerl.lolstats.service.summoner;

import com.nwerl.lolstats.web.dto.riotapi.summoner.RiotSummonerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

@Slf4j
@Retryable(value = HttpServerErrorException.class, maxAttempts = 3)
@Component
public class SummonerApiRestCaller implements SummonerApiCaller{
    private final RestTemplate restTemplate;

    public SummonerApiRestCaller(RestTemplateBuilder restTemplateBuilder,
                                 @Value("${apikey}") String apiKey,
                                 @Value("${riotgames.protocol}") String scheme,
                                 @Value("${riotgames.hostname}") String hostname) {

        this.restTemplate = restTemplateBuilder.requestFactory(()->new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(20000))
                .setReadTimeout(Duration.ofMillis(20000))
                .build();

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory
                (UriComponentsBuilder.newInstance()
                        .scheme(scheme).host(hostname).path("lol").queryParam("api_key", apiKey)));
    }

    public RiotSummonerDto fetchSummonerFromRiotApiById(String id) {
        log.info("Call RiotApi to Get SummonerInfo : {}", id);

        return restTemplate.getForObject(String.format(SUMMONER_BY_ID_URI, id), RiotSummonerDto.class);
    }

    public RiotSummonerDto fetchSummonerFromRiotApiByName(String name) {
        log.info("Call RiotApi to Get SummonerInfo : {}", name);

        return restTemplate.getForObject(String.format(SUMMONER_BY_NAME_URI, name), RiotSummonerDto.class);
    }
}
