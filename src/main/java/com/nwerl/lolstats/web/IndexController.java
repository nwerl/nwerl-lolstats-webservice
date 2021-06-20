package com.nwerl.lolstats.web;

import com.nwerl.lolstats.service.league.LeagueService;
import com.nwerl.lolstats.service.match.MatchReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final LeagueService leagueService;
    private final MatchReferenceService matchReferenceService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("leagueList", leagueService.getLeagueRanking());
        return "index";
    }

    @GetMapping("/summoner/{summonerName}/matches")
    public String summonerMatchList(@PathVariable String summonerName) {
        return "summonerInfo";
    }
}