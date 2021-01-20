package com.nwerl.lolstats.web.dto;

import lombok.Data;


import java.util.List;

@Data
public class MatchlistDto {
    private Integer startIndex;
    private Integer totalGames;
    private Integer endIndex;
    private List<MatchReferenceDto> matches;
}