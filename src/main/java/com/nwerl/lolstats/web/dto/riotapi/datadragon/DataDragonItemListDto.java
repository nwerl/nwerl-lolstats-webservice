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
public class DataDragonItemListDto {
    private Map<String, Object> data;

    public List<ItemDto> toItemDtoList() {
        return data.keySet().stream().map(ItemDto::new).collect(Collectors.toList());
    }
}
