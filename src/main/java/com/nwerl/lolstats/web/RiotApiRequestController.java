package com.nwerl.lolstats.web;

import com.nwerl.lolstats.service.FeaturedGameInfoService;
import com.nwerl.lolstats.service.LeagueService;
import com.nwerl.lolstats.service.MatchService;
import com.nwerl.lolstats.service.SummonerService;
import com.nwerl.lolstats.web.dto.riotApi.featuredgame.FeaturedGameInfoDto;
import com.nwerl.lolstats.web.dto.riotApi.match.MatchDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.MatchReferenceDto;
import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RiotApiRequestController {
    private final SummonerService summonerService;
    private final MatchService matchService;
    private final FeaturedGameInfoService featuredGameInfoService;
    private final LeagueService leagueService;

    @GetMapping("/api/v1/get/summonerinfo/{name}")
    public SummonerDto getSummonerInfo(@PathVariable String name) {
        return summonerService.findByName(name);
    }

    @GetMapping("/api/v1/get/matchlists/{name}")
    public MatchReferenceDto getLastMatchReference(@PathVariable String name) {
        return matchService.getLastMatchReferenceByName(name);
    }

    @GetMapping("/api/v1/get/matches/{name}")
    public MatchDto getMatches(@PathVariable String name) {
        return matchService.updateMatchByName(name);
    }

    @GetMapping("/api/v1/get/featured-games")
    public List<FeaturedGameInfoDto> getFeaturedGames() {
        return featuredGameInfoService.updateFeaturedGames();
    }

    @GetMapping("/api/v1/get/leagues/challenger")
    public List<LeagueItemDto> getChallengerLeagueItems() {
        return leagueService.updateChallengerLeagueList().getEntries();
    }
}