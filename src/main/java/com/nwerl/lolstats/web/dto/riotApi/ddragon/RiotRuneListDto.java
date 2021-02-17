package com.nwerl.lolstats.web.dto.riotApi.ddragon;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class RiotRuneListDto {
    private String icon;

    private List<List<RiotRunesDto>> slots;

    public static class RiotRunesDto {
        private String icon;
    }
}
