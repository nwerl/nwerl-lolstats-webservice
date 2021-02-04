package com.nwerl.lolstats.web.dto.riotApi.league;

import com.nwerl.lolstats.web.domain.league.LeagueItem;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LeagueItemDto {
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