package com.nwerl.lolstats.web.dto.riotApi.league;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeagueListDto {
    private List<LeagueItemDto> entries;
    private Integer index;

    public LeagueListDto(List<LeagueItemDto> entries) {
        this.entries = entries;
        this.index = 0;
    }

    public LeagueItemDto traversal() {
        LeagueItemDto entry = entries.get(index);
        index = (index+1<entries.size()) ? index+1 : 0;

        return entry;
    }
}
