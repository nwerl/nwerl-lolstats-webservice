package com.nwerl.lolstats.service.summoner;

import com.nwerl.lolstats.web.dto.riotapi.summoner.RiotSummonerDto;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;

@Retryable(value = HttpServerErrorException.class, maxAttempts = 3)
public interface SummonerApiCaller {
    public static final String summonerById = "/summoner/v4/summoners/%s";
    public static final String summonerByName = "/summoner/v4/summoners/by-name/%s";

    RiotSummonerDto fetchSummonerFromRiotApiByName(String name);
    RiotSummonerDto fetchSummonerFromRiotApiById(String id);
}