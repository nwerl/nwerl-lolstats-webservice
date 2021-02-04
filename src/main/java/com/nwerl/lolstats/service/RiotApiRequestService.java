package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.dto.riotApi.featuredgame.FeaturedGameInfoDto;
import com.nwerl.lolstats.web.dto.riotApi.featuredgame.FeaturedGamesDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueListDto;
import com.nwerl.lolstats.web.dto.riotApi.match.MatchDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.MatchListDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.MatchReferenceDto;
import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.info("Call RiotApi to Get SummonerInfo");
        return restTemplate.getForObject(url, SummonerDto.class);
    }

    public MatchReferenceDto getLastMatchReference(String accountId) {
        String url = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/"
                + accountId
                + "?endIndex=1"
                + "&api_key=" + apiKey;
        log.info("Call RiotApi to Get MatchReferences");
        return restTemplate.getForObject(url, MatchListDto.class).getMatches().get(0);
    }

    public MatchDto getMatchByGameId(Long gameId) {
        String url = "https://kr.api.riotgames.com/lol/match/v4/matches/" + gameId + "?api_key=" + apiKey;
        return restTemplate.getForObject(url, MatchDto.class);
    }

    public List<FeaturedGameInfoDto> getFeaturedGameInfo() {
        String url = "https://kr.api.riotgames.com/lol/spectator/v4/featured-games" + "?api_key=" + apiKey;
        log.info("Call RiotApi to Get FeaturedGames");
        return restTemplate.getForObject(url, FeaturedGamesDto.class).getGameList();
    }

    public List<LeagueItemDto> getChallengerLeagueItem() {
        String url = "https://kr.api.riotgames.com/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5" + "?api_key=" + apiKey;
        log.info("Call RiotApi to Get ChallengerLeagueItem");
        return restTemplate.getForObject(url, LeagueListDto.class).getEntries();
    }
}