package com.nwerl.lolstats.web.dto.riotApi.datadragon;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class DataDragonChampionListDto {
    private Map<String, RiotChampionDto> data;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class RiotChampionDto {
        private String id;
        private Long key;
    }

    public Map<Long, String> toMap() {
        return data.values().stream().collect(Collectors.toMap(RiotChampionDto::getKey, RiotChampionDto::getId));
    }
}
