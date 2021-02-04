package com.nwerl.lolstats.web.domain.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Document(collection = "matchList")
public class MatchList {
    @Id
    private String accountId;
    private String summonerName;

    private List<MatchReference> matchReferences;


    public void addMatchReferences(List<MatchReference> matchReferences) {
        this.matchReferences.addAll(matchReferences);
    }
}
