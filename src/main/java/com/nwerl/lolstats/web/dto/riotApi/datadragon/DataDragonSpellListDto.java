package com.nwerl.lolstats.web.dto.riotApi.datadragon;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class DataDragonSpellListDto {
    private Map<String, RiotSpellDto> data;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class RiotSpellDto {
        private Long key;
        private String id;
    }

    public Map<Long, String> toMap() {
        return data.values().stream().collect(Collectors.toMap(RiotSpellDto::getKey, RiotSpellDto::getId));
    }
}
