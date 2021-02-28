package com.nwerl.lolstats.service.summoner;

import com.nwerl.lolstats.web.dto.riotapi.summoner.RiotSummonerDto;

public interface SummonerApiCaller {
    RiotSummonerDto fetchSummonerFromRiotApiByName(String name);
    RiotSummonerDto fetchSummonerFromRiotApiById(String id);
}