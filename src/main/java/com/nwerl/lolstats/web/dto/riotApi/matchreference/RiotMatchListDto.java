package com.nwerl.lolstats.web.dto.riotApi.matchreference;

import lombok.*;


import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiotMatchListDto {
    private Integer startIndex;
    private Integer totalGames;
    private Integer endIndex;
    private List<RiotMatchReferenceDto> matches;
}