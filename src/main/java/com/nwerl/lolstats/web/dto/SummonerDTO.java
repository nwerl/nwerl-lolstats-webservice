package com.nwerl.lolstats.web.dto;

import com.nwerl.lolstats.web.domain.summoner.Summoner;
import lombok.Builder;

import javax.xml.stream.FactoryConfigurationError;


public class SummonerDTO {
    private String id;
    private String accountId;
    private String puuid;
    private String name;


    private Long profileIconId;
    private Long revisionDate;
    private Long summonerLevel;

    @Builder
    public SummonerDTO(String id, String accountId, String puuid, String name,
                       Long profileIconId, Long revisionDate, Long summonerLevel) {
        this.id = id;
        this.accountId = accountId;
        this.puuid = puuid;
        this.name = name;
        this.profileIconId = profileIconId;
        this.revisionDate = revisionDate;
        this.summonerLevel = summonerLevel;
    }

    public Summoner toEntity() {
        return Summoner.builder()
                .id(id)
                .accountId(accountId)
                .puuid(puuid)
                .name(name)
                .profileIconId(profileIconId)
                .revisionDate(revisionDate)
                .summonerLevel(summonerLevel)
                .build();
    }
}