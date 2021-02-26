package com.nwerl.lolstats.service.league;

import com.nwerl.lolstats.web.dto.riotApi.league.LeagueListDto;

public interface LeagueApiCaller {
    LeagueListDto callApiChallengerLeagueItem();
}