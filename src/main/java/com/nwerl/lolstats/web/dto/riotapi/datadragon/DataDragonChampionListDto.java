package com.nwerl.lolstats.web.dto.riotapi.datadragon;


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
    private Map<String, ChampionDto> data;

    public List<ChampionDto> toChampionDtoList() {
        return data.values().stream().map(c -> new ChampionDto(c.getId(), c.getKey())).collect(Collectors.toList());
    }
}
