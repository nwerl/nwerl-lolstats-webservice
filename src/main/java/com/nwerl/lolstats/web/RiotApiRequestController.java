package com.nwerl.lolstats.web;

import com.nwerl.lolstats.service.MatchReferenceService;
import com.nwerl.lolstats.service.SummonerService;
import com.nwerl.lolstats.web.dto.SummonerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RiotApiRequestController {
    private final SummonerService summonerService;
    private final MatchReferenceService matchReferenceService;

    @GetMapping("/api/v1/get/summonerinfo/{name}")
    public SummonerDto getSummonerInfo(@PathVariable String name) {
        return summonerService.findByName(name);
    }

        @GetMapping("/api/v1/get/matchlists/{name}")
    public Boolean getMatchReferences(@PathVariable String name) {
        return matchReferenceService.updateMatchReferenceByName(name);
    }
}
