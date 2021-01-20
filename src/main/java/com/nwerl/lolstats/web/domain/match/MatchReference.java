package com.nwerl.lolstats.web.domain.match;

import com.nwerl.lolstats.web.dto.MatchReferenceDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "matchReference")
public class MatchReference {
    private Long gameId;
    private String role;
    private Integer season;
    private String platformId;
    private Integer champion;
    private Integer queue;
    private String lane;
    private Long timestamp;

    public MatchReferenceDto toDto() {
        return MatchReferenceDto.builder()
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
