package com.nwerl.lolstats.service.league;

import com.nwerl.lolstats.web.dto.riotApi.league.LeagueListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.server.HttpServer;

import java.time.Duration;

@Slf4j
@Retryable(value = HttpServerErrorException.class, maxAttempts = 3)
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

    public LeagueListDto callApiChallengerLeagueItem() {
        log.info("Call RiotApi to Get ChallengerLeagueItem");

        return restTemplate.getForObject(challengerLeagueUri, LeagueListDto.class);
    }
}
