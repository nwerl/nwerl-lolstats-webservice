package com.nwerl.lolstats.service.match;

import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchListDto;
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
public class MatchApiRestCaller implements MatchApiCaller{
    private final RestTemplate restTemplate;


    public MatchApiRestCaller(RestTemplateBuilder restTemplateBuilder,
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

    public RiotMatchListDto fetchMatchListFromRiotApi(String accountId) {
        log.info("Call RiotApi to Get MatchReferences");

        return restTemplate.getForObject(String.format(matchListUri, accountId), RiotMatchListDto.class);
    }

    public RiotMatchDto fetchMatchFromRiotApi(Long gameId) {
        log.info("Call RiotApi to Get Match {}", gameId);

        return restTemplate.getForObject(String.format(matchUri, String.valueOf(gameId)), RiotMatchDto.class);
    }
}
