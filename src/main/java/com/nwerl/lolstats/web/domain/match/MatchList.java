package com.nwerl.lolstats.web.domain.match;

import com.nwerl.lolstats.web.dto.view.MatchListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
@Document(collection = "matchList")
public class MatchList {
    @Id
    private String accountId;

    private List<MatchReference> matchReferences;

    public MatchListDto of() {
        return MatchListDto.builder()
                .accountId(accountId)
                .matchReferences(
                        this.matchReferences.stream()
                        .map(matchReferences->
                                MatchListDto.MatchReferenceDto.builder()
                                .gameId(matchReferences.getGameId())
                                .timeStamp(matchReferences.getTimeStamp())
                                .build())
                        .collect(Collectors.toList())
                )
                .build();
    }
}
