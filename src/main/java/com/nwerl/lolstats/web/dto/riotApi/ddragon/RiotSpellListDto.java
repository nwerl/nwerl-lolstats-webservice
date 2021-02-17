package com.nwerl.lolstats.web.dto.riotApi.ddragon;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class RiotSpellListDto {
    private Map<String, Object> data;
}
