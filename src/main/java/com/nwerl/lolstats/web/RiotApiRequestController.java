package com.nwerl.lolstats.web;

import com.nwerl.lolstats.service.league.LeagueService;
import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.service.summoner.SummonerService;
import com.nwerl.lolstats.web.dto.riotApi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.RiotMatchReferenceDto;
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
    private final LeagueService leagueService;

    @GetMapping("/api/v1/get/summonerinfo/{name}")
    public String getSummonerInfo(@PathVariable String name) {
        return summonerService.findAccountIdByName(name);
    }

    @GetMapping("/api/v1/get/matchlists/{name}")
    public RiotMatchReferenceDto getLastMatchReference(@PathVariable String name) {
        return matchService.callApiLastMatchReference(name);
    }

    @GetMapping("/api/v1/get/matches/{gameId}")
    public RiotMatchDto getMatches(@PathVariable Long gameId) {
        return matchService.callApiMatchByGameId(gameId);
    }

    @GetMapping("/api/v1/get/leagues/challenger")
    public List<LeagueItemDto> getChallengerLeagueItems() {
        return leagueService.updateChallengerLeagueList().getEntries();
    }
}