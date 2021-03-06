package com.nwerl.lolstats.web.dto.riotapi.league;

import com.nwerl.lolstats.web.domain.league.LeagueItem;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiotLeagueItemDto {
    private String summonerId;
    private String summonerName;
    private Integer leaguePoints;
    private String rank;
    private Integer wins;
    private Integer losses;
    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    private Boolean hotStreak;

    public LeagueItem toEntity() {
        return LeagueItem.builder()
                .summonerId(summonerId)
                .summonerName(summonerName)
                .leaguePoints(leaguePoints)
                .rank(rank)
                .wins(wins)
                .losses(losses)
                .veteran(veteran)
                .inactive(inactive)
                .freshBlood(freshBlood)
                .hotStreak(hotStreak)
                .build();
    }
}