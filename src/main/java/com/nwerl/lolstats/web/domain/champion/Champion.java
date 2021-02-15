package com.nwerl.lolstats.web.domain.champion;

import com.nwerl.lolstats.web.dto.view.ChampionDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Builder
@Getter
@Document(collection = "champion")
public class Champion {
    @Id
    private Long id;
    private String name;
    private byte[] thumbNail;

    public Champion(Long id, String name, byte[] thumbNail) {
        this.id = id;
        this.name = name;
        this.thumbNail = thumbNail;
    }

    public void setThumbNail(byte[] thumbNail) {
        this.thumbNail = thumbNail;
    }

    public ChampionDto of() {
        return ChampionDto.builder()
                .id(id)
                .name(name)
                .thumbNail(thumbNail)
                .build();
    }
}
