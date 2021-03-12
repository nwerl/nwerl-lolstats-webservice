package com.nwerl.lolstats.web.dto.riotapi.league;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiotLeagueListDto {
    private List<RiotLeagueItemDto> entries;
    private Integer index;

    public RiotLeagueListDto(List<RiotLeagueItemDto> entries) {
        this.entries = entries;
        this.index = 0;
    }
}
