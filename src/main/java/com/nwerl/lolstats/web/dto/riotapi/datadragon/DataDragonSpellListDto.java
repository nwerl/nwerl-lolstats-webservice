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
public class DataDragonSpellListDto {
    private Map<String, SpellDto> data;

    public List<SpellDto> toSpellDtoList() {
        return data.values().stream().map(s -> new SpellDto(s.getKey(), s.getId())).collect(Collectors.toList());
    }
}
