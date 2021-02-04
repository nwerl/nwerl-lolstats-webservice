package com.nwerl.lolstats.web.domain.league;

import com.mongodb.lang.Nullable;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Builder
@AllArgsConstructor
@Getter
@Document(collection = "leagueItem")
public class LeagueItem {
    @Id
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

    @Nullable
    private String accountId;
    @Nullable
    private String recentMatchId;
    @Nullable
    private String recentMatchTime;

    public LeagueItemDto of() {
        return LeagueItemDto.builder()
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