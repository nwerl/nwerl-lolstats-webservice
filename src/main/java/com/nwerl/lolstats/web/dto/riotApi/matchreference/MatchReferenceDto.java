package com.nwerl.lolstats.web.dto.riotApi.matchreference;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MatchReferenceDto {
    private Long gameId;
    private String role;
    private Integer season;
    private String platformId;
    private Integer champion;
    private Integer queue;
    private String lane;
    private Long timestamp;
}