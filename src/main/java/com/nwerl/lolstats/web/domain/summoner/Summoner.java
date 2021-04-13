package com.nwerl.lolstats.web.domain.summoner;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "summoner")
public class Summoner implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        this.summonerLevel = summonerLevel;
    }

    public void modifyName(String name) {
        this.name = name;
    }
}
