package com.nwerl.lolstats.web.dto.view;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Builder
@Data
public class ChampionDto {
    @Id
    private Long id;
    private String name;
    private byte[] thumbNail;
}
