package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.dto.MatchReferenceDto;
import com.nwerl.lolstats.web.dto.MatchlistDto;
import com.nwerl.lolstats.web.dto.SummonerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;

@Service
public class RiotApiRequestService {
    private final String apiKey;
    private final RestTemplate restTemplate;

    @Autowired
    public RiotApiRequestService(@Value("${apikey}")String apiKey,
                                 RestTemplateBuilder restTemplateBuilder) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplateBuilder.requestFactory(()->new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(20000))
                .setReadTimeout(Duration.ofMillis(20000))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .build();
    }

    public SummonerDto getSummonerInfoByName(String name) {
        String url = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + name + "?api_key=" + apiKey;
        return restTemplate.getForObject(url, SummonerDto.class);
    }

    public List<MatchReferenceDto> getMatchReferencesByAccountId(String accountId) {
        String url = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/"
                + accountId
                + "?api_key=" + apiKey;
        return restTemplate.getForObject(url, MatchlistDto.class).getMatches();
    }

    public Match getMatchByGameId(Long gameId) {
        String url = "https://kr.api.riotgames.com/lol/match/v4/matches/" + gameId + "?api_key=" + apiKey;
        return restTemplate.getForObject(url, Match.class);
    }
}