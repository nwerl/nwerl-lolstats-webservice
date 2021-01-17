package com.nwerl.lolstats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwerl.lolstats.web.dto.SummonerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class RiotApiRequestService {
    private final String apiKey;
    private final ApiRequestService<String> apiRequestService;
    private final ObjectMapper objectMapper;

    @Autowired
    public RiotApiRequestService(@Value("${apikey}")String apiKey,
                               ApiRequestService<String> apiRequestService,
                               ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.apiRequestService = apiRequestService;
        this.objectMapper = objectMapper;
    }


    public SummonerDTO getSummonerInfoByName(String name) {
        String url = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+name+"?api_key="+apiKey;
        return objectMapper.convertValue(apiRequestService.get(url, HttpHeaders.EMPTY).getBody(), SummonerDTO.class);
    }
}
