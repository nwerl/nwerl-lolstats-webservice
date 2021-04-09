package com.nwerl.lolstats.web.dto.riotapi.datadragon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ItemDto {
    private String id;

    public ItemDto(String id) {
        this.id = id;
    }
}
