package com.nwerl.lolstats.web.domain.summoner;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection="summoner")
public class Summoner {
    private String id;
    private String accountId;
    private String puuid;
    private String name;

    private Long profileIconId;
    private Long revisionDate;
    private Long summonerLevel;

    @Builder
    public Summoner(String id, String accountId, String puuid, String name,
                    Long profileIconId, Long revisionDate, Long summonerLevel) {
        this.id = id;
        this.accountId = accountId;
        this.puuid = puuid;
        this.name = name;
        this.profileIconId = profileIconId;
        this.revisionDate = revisionDate;
        this.summonerLevel = summonerLevel;
    }

    public void modifyName(String name) {
        this.name = name;
    }
}
