package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.featuredgame.FeaturedGameInfo;
import com.nwerl.lolstats.web.domain.featuredgame.FeaturedGameInfoRepository;
import com.nwerl.lolstats.web.dto.riotApi.featuredgame.FeaturedGameInfoDto;
import com.nwerl.lolstats.web.dto.riotApi.featuredgame.FeaturedGamesDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FeaturedGameInfoService {
    private final FeaturedGameInfoRepository featuredGameInfoRepository;
    private final UriComponentsBuilder uriComponentsBuilder;
    private final RestTemplate restTemplate;

    public List<FeaturedGameInfoDto> updateFeaturedGames() {
        log.info("Update FeaturedGames");
        return featuredGameInfoRepository.saveAll(getFeaturedGameInfo().getGameList().stream()
                    .map(FeaturedGameInfoDto::toEntity).collect(Collectors.toList()))
                .stream()
                .map(FeaturedGameInfo::toDto).collect(Collectors.toList());
    }

    public FeaturedGamesDto getFeaturedGameInfo() {
        log.info("Call RiotApi to Get FeaturedGames");
        String uri = uriComponentsBuilder
                .path("/spectator/v4/featured-games")
                .build().toString();

        return restTemplate.getForObject(uri, FeaturedGamesDto.class);
    }


}
