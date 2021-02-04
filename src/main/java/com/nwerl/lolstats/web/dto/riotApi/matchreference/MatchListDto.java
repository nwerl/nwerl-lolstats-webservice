package com.nwerl.lolstats.web.dto.riotApi.matchreference;

import lombok.Builder;
import lombok.Data;


import java.util.List;

@Builder
@Data
public class MatchListDto {
    private Integer startIndex;
    private Integer totalGames;
    private Integer endIndex;
    private List<MatchReferenceDto> matches;
}