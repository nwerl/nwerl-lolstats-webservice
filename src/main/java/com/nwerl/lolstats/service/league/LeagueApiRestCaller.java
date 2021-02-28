package com.nwerl.lolstats.service.league;

import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueListDto;
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
@Component
public class LeagueApiRestCaller implements LeagueApiCaller{
    private final RestTemplate restTemplate;
    private static final String challengerLeagueUri = "/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5";

    public LeagueApiRestCaller(RestTemplateBuilder restTemplateBuilder,
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

    @Retryable(value = HttpServerErrorException.class, maxAttempts = 3)
    public RiotLeagueListDto fetchChallengerLeagueListFromRiotApi() {
        log.info("Call RiotApi to Get ChallengerLeagueList");

        return restTemplate.getForObject(challengerLeagueUri, RiotLeagueListDto.class);
    }
}
