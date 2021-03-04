package com.nwerl.lolstats.service.match;

import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchListDto;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;

@Retryable(value = HttpServerErrorException.class, maxAttempts = 3)
public interface MatchApiCaller {
    public static final String matchListUri = "/match/v4/matchlists/by-account/%s";
    public static final String matchUri = "/match/v4/matches/%s";

    RiotMatchListDto fetchMatchListFromRiotApi(String accountId);
    RiotMatchDto fetchMatchFromRiotApi(Long gameId);
}