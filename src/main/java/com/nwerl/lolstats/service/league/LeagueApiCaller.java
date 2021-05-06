package com.nwerl.lolstats.service.league;

import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueListDto;


public interface LeagueApiCaller {
    public static final String CHALLENGER_LEAGUE_URI = "/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5";


    RiotLeagueListDto fetchChallengerLeagueListFromRiotApi();
}