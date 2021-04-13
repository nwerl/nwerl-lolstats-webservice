package com.nwerl.lolstats.web.domain.match;

import com.nwerl.lolstats.web.dto.view.MatchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class Player {
    private String accountId;
    private String summonerName;

    private Integer teamId;
    private Boolean win;

    private Integer championId;
    private Integer spell1Id;
    private Integer spell2Id;
    private Integer perkPrimaryStyle;
    private Integer perkSubStyle;

    private Integer kills;
    private Integer deaths;
    private Integer assists;

    private Integer champLevel;
    private Integer totalMinionsKilled;

    public Integer item0;
    public Integer item1;
    public Integer item2;
    public Integer item3;
    public Integer item4;
    public Integer item5;
    public Integer item6;
    private Integer visionWardsBoughtInGame;

    private Long totalDamageDealtToChampions;
    private Integer goldEarned;

    public MatchDto.PlayerDto of() {
        return  MatchDto.PlayerDto.builder()
                .accountId(accountId)
                .spell1Id(spell1Id)
                .spell2Id(spell2Id)
                .summonerName(summonerName)
                .assists(assists)
                .championId(championId)
                .champLevel(champLevel)
                .deaths(deaths)
                .kills(kills)
                .goldEarned(goldEarned)
                .item0(item0)
                .item1(item1)
                .item2(item2)
                .item3(item3)
                .item4(item4)
                .item5(item5)
                .item6(item6)
                .perkPrimaryStyle(perkPrimaryStyle)
                .perkSubStyle(perkSubStyle)
                .teamId(teamId)
                .totalDamageDealtToChampions(totalDamageDealtToChampions)
                .totalMinionsKilled(totalMinionsKilled)
                .visionWardsBoughtInGame(visionWardsBoughtInGame)
                .win(win)
                .build();
    }
}
