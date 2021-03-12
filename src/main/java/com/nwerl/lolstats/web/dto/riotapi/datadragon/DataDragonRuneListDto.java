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
        private List<RiotRunesDto> runes;

        @NoArgsConstructor
        @Setter
        @Getter
        public static class RiotRunesDto {
            private Long id;
            private String icon;
        }
    }

    public String getIcon() {
        return icon.substring(icon.lastIndexOf("/")+1, icon.lastIndexOf("."));
    }
}
