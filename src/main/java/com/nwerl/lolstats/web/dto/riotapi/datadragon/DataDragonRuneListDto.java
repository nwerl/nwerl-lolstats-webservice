package com.nwerl.lolstats.web.dto.riotapi.datadragon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class DataDragonRuneListDto {
    private Long id;
    private String icon;

    private List<RiotSlotsDto> slots;

    @NoArgsConstructor
    @Setter
    @Getter
    public static class RiotSlotsDto {
        private List<RuneDto> runes;
    }

    public String getIcon() {
        return icon.substring(icon.lastIndexOf("/")+1, icon.lastIndexOf("."));
    }
}
