package com.nwerl.lolstats.service.summoner;

import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
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

    public SummonerDto callApiSummonerInfoBySummonerId(String id) {
        log.info("Call RiotApi to Get SummonerInfo : {}", id);
        String uri = UriComponentsBuilder.newInstance().path("/summoner/v4/summoners/")
                .path(id)
                .build().toString();
        return restTemplate.getForObject(uri, SummonerDto.class);
    }

    public SummonerDto callApiSummonerInfoByName(String name) {
        log.info("Call RiotApi to Get SummonerInfo : {}", name);
        String uri = UriComponentsBuilder.newInstance().path("/summoner/v4/summoners/by-name/")
                .path(name)
                .build().toString();
        return restTemplate.getForObject(uri, SummonerDto.class);
    }
}
