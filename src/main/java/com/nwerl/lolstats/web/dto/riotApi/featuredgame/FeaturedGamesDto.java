package com.nwerl.lolstats.web.dto.riotApi.featuredgame;

import lombok.Data;

import java.util.List;

@Data
public class FeaturedGamesDto {
    private List<FeaturedGameInfoDto> gameList;
    private Long clientRefreshInterval;
}