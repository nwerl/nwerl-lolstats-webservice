package com.nwerl.lolstats.web.dto.riotapi.datadragon;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class DataDragonItemListDto {
    private Map<String, Object> data;

    public List<String> toList() {
        return new ArrayList<>(data.keySet());
    }
}
