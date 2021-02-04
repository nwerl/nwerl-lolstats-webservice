package com.nwerl.lolstats.web.domain.match;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Builder
@AllArgsConstructor
@Getter
public class MatchReference {
    @Id
    private Long gameId;
    private Long timeStamp;
}
