package com.nwerl.lolstats.web.dto.riotApi.datadragon;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class RiotItemListDto {
    private Map<String, Object> data;
}
