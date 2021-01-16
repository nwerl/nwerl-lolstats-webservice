package com.nwerl.lolstats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwerl.lolstats.web.dto.SummonerDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class SummonerInfoService {
    private final String apiKey;
    private final ApiRequestService<String> apiRequestService;

    @Autowired
    public SummonerInfoService(@Value("${apikey}")String apiKey, ApiRequestService<String> apiRequestService) {
        this.apiKey = apiKey;
        this.apiRequestService = apiRequestService;
    }


    public SummonerDTO getSummonerInfoByName(String name) throws IOException {
        name = URLEncoder.encode(name, "UTF-8");
        String url = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+name+"?api_key="+apiKey;
        System.out.println(url);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(apiRequestService.get(url, HttpHeaders.EMPTY).getBody(), SummonerDTO.class);
    }
}
