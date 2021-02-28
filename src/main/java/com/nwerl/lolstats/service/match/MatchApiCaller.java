package com.nwerl.lolstats.service.match;

import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchListDto;

public interface MatchApiCaller {
    RiotMatchListDto fetchMatchListFromRiotApi(String accountId);
    RiotMatchDto fetchMatchFromRiotApi(Long gameId);
}