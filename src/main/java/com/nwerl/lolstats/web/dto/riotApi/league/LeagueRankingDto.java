package com.nwerl.lolstats.web.dto.riotApi.league;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LeagueRankingDto {
    private Integer rank;
    private String summonerName;
    private Integer leaguePoints;
}
