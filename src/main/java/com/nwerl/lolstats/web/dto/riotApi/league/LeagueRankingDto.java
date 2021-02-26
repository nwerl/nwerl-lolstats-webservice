package com.nwerl.lolstats.web.dto.riotApi.league;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeagueRankingDto {
    private Integer rank;
    private String summonerName;
    private Integer leaguePoints;
}
