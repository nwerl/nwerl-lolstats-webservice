package com.nwerl.lolstats.service.league;

import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueListDto;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;

public interface LeagueApiCaller {
    @Retryable(value = HttpServerErrorException.class, maxAttempts = 3)
    RiotLeagueListDto fetchChallengerLeagueListFromRiotApi();
}