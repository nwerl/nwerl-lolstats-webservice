package com.nwerl.lolstats.web.dto.riotapi.datadragon;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class RuneDto {
    private Long id;
    private String icon;

    public RuneDto(Long id, String icon) {
        this.id = id;
        this.icon = icon;
    }
}