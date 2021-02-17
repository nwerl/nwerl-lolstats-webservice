package com.nwerl.lolstats.web.dto.riotApi.ddragon;

import com.nwerl.lolstats.web.domain.champion.Champion;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class RiotChampionListDto {
    private Map<String, RiotChampionDto> data;

    @NoArgsConstructor
    @Data
    public static class RiotChampionDto {
        private String id;
        private Long key;

        public Champion toEntity() {
            return Champion.builder().id(key).name(id).build();
        }
    }

    public List<Champion> toEntity() {
        return data.values().stream().map(RiotChampionDto::toEntity).collect(Collectors.toList());
    }
}
