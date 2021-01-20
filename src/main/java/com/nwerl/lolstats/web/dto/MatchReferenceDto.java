package com.nwerl.lolstats.web.dto;

import com.nwerl.lolstats.web.domain.match.MatchReference;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MatchReferenceDto{
    private Long gameId;
    private String role;
    private Integer season;
    private String platformId;
    private Integer champion;
    private Integer queue;
    private String lane;
    private Long timestamp;

    public MatchReference toEntity() {
        return MatchReference.builder()
                .gameId(gameId)
                .role(role)
                .season(season)
                .platformId(platformId)
                .champion(champion)
                .queue(queue)
                .lane(lane)
                .timestamp(timestamp)
                .build();
    }
}
