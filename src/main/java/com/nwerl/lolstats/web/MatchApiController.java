package com.nwerl.lolstats.web;

import com.nwerl.lolstats.service.match.MatchReferenceService;
import com.nwerl.lolstats.web.dto.view.MatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MatchApiController {
    private final MatchReferenceService matchReferenceService;

    @GetMapping("/api/{summonerName}/matches")
    public List<MatchDto> summonerMatchList(@PathVariable String summonerName,
                                            @RequestParam(value = "id", defaultValue = MatchReferenceService.NOT_INIT_GAMECREATION + "") Long gameCreation) {
        return matchReferenceService.getMatchPageBySummonerName(summonerName, gameCreation);
    }
}