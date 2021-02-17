package com.nwerl.lolstats.web.dto.riotApi.ddragon;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Data
public class RiotItemListDto {
    private Map<String, Object> data;
}
