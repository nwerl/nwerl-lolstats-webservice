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

    public RiotLeagueItemDto traversal() {
        RiotLeagueItemDto entry = entries.get(index);
        index = (index+1<entries.size()) ? index+1 : 0;

        return entry;
    }
}
