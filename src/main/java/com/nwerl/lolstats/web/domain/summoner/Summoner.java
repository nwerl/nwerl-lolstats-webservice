package com.nwerl.lolstats.web.domain.summoner;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Data
@Document(collection="summoner")
public class Summoner {

    @Id
    private Long id;

    private String summonerId;
    private String accountId;
    private String puuid;
    private String name;


    private Long profileIconId;
    private Long revisionDate;
    private Long summonerLevel;

    @Builder
    public Summoner(String summonerId, String accountId, String puuid, String name,
                    Long profileIconId, Long revisionDate, Long summonerLevel) {
        this.summonerId = summonerId;
        this.accountId = accountId;
        this.puuid = puuid;
        this.name = name;
        this.profileIconId = profileIconId;
        this.revisionDate = revisionDate;
        this.summonerId = summonerId;
    }
}
