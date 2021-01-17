package com.nwerl.lolstats.web;

import com.nwerl.lolstats.service.RiotApiRequestService;
import com.nwerl.lolstats.service.SummonerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RiotApiRequestController {
    private final RiotApiRequestService riotApiRequestService;
    private final SummonerService summonerService;

    @GetMapping("/api/v1/get/summonerinfo/{name}")
    public String saveSummonerInfo(@PathVariable String name) {
        return summonerService.save(riotApiRequestService.getSummonerInfoByName(name));
    }
}
