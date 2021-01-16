package com.nwerl.lolstats.web;

import com.nwerl.lolstats.service.SummonerInfoService;
import com.nwerl.lolstats.web.dto.SummonerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@RestController
public class RiotApiRequestController {
    private final SummonerInfoService summonerInfoService;

    @GetMapping("/api/v1/riot")
    public SummonerDTO getSummonerInfo() throws IOException {
        return summonerInfoService.getSummonerInfoByName("Vehumet");
    }
}
