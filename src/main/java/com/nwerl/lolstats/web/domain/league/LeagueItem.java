package com.nwerl.lolstats.web.domain.league;

import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "league_item")
public class LeagueItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Builder
    public LeagueItem(String summonerId, String summonerName, Integer leaguePoints, String rank, Integer wins,
                      Integer losses, Boolean veteran, Boolean inactive, Boolean freshBlood, Boolean hotStreak) {
        this.summonerId = summonerId;
        this.summonerName = summonerName;
        this.leaguePoints = leaguePoints;
        this.rank = rank;
        this.wins = wins;
        this.losses = losses;
        this.veteran = veteran;
        this.inactive = inactive;
        this.freshBlood = freshBlood;
        this.hotStreak = hotStreak;
    }

    public RiotLeagueItemDto of() {
        return RiotLeagueItemDto.builder()
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