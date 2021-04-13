package com.nwerl.lolstats.web.dto.riotapi.datadragon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SpellDto {
    private Long key;
    private String id;

    public SpellDto(Long key, String id) {
        this.key = key;
        this.id = id;
    }
}
