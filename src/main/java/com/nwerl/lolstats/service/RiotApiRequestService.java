package com.nwerl.lolstats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.domain.match.MatchReferenceRepository;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.MatchReferenceDto;
import com.nwerl.lolstats.web.dto.MatchlistDto;
import com.nwerl.lolstats.web.dto.SummonerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiotApiRequestService {
    private final String apiKey;
    private final ApiRequestService<String> apiRequestService;
    private final ObjectMapper objectMapper;
    private final SummonerRepository summonerRepository;

    @Autowired
    public RiotApiRequestService(@Value("${apikey}")String apiKey,
                                 ApiRequestService<String> apiRequestService,
                                 ObjectMapper objectMapper,
                                 SummonerRepository summonerRepository) {
        this.apiKey = apiKey;
        this.apiRequestService = apiRequestService;
        this.objectMapper = objectMapper;
        this.summonerRepository = summonerRepository;
    }

    public SummonerDto getSummonerInfoByName(String name) {
        String url = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + name + "?api_key=" + apiKey;
        return objectMapper.convertValue(apiRequestService.get(url, HttpHeaders.EMPTY).getBody(), SummonerDto.class);
    }

    public List<MatchReferenceDto> getMatchReferencesByName(String name) {
        String url = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/" + summonerRepository.findByName(name).getAccountId() + "?api_key=" + apiKey;
        return objectMapper.convertValue(apiRequestService.get(url, HttpHeaders.EMPTY).getBody(), MatchlistDto.class).getMatches();
    }

    public Match getMatchByGameId(Long gameId) {
        String url = "https://kr.api.riotgames.com/lol/match/v4/matches/" + gameId + "?api_key=" + apiKey;
        return objectMapper.convertValue(apiRequestService.get(url, HttpHeaders.EMPTY).getBody(), Match.class);
    }
}