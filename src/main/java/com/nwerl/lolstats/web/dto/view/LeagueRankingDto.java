package com.nwerl.lolstats.web.dto.view;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeagueRankingDto implements Serializable {
    private static final long serialVersionUID = 5766201181682306320L;
    private Integer rank;
    private String summonerName;
    private Integer leaguePoints;
}
