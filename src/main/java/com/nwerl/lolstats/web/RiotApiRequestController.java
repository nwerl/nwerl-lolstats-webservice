package com.nwerl.lolstats.web;

import com.nwerl.lolstats.service.SummonerInfoService;
import com.nwerl.lolstats.web.dto.SummonerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RiotApiRequestController {
    private final SummonerInfoService summonerInfoService;

    @GetMapping("/api/v1/get/summonerinfo/{name}")
    public SummonerDTO getSummonerInfo(@PathVariable String name) {
        return summonerInfoService.getSummonerInfoByName(name);
    }
}
