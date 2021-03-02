package com.nwerl.lolstats.web.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class MatchListDto {
    @Id
    private String accountId;

    private List<MatchReferenceDto> matchReferences;

    @Builder
    @Data
    public static class MatchReferenceDto {
        @Id
        private Long gameId;
        private Long timeStamp;
    }
}