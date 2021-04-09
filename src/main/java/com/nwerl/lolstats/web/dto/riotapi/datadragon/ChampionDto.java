package com.nwerl.lolstats.web.dto.riotapi.datadragon;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChampionDto {
    private String id;
    private Long key;

    public ChampionDto(String id, Long key) {
        this.id = id;
        this.key = key;
    }
}
