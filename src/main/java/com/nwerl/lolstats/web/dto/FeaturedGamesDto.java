package com.nwerl.lolstats.web.dto;

import com.nwerl.lolstats.web.domain.match.FeaturedGameInfo;
import lombok.Data;

import java.util.List;

@Data
public class FeaturedGamesDto {
    private List<FeaturedGameInfoDto> gameList;
    private Long clientRefreshInterval;
}