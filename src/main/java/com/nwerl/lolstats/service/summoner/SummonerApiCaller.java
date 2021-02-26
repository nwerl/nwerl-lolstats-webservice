package com.nwerl.lolstats.service.summoner;

import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;

public interface SummonerApiCaller {
    SummonerDto callApiSummonerInfoByName(String name);
    SummonerDto callApiSummonerInfoBySummonerId(String id);
}