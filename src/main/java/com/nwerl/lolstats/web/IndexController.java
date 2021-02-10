package com.nwerl.lolstats.web;

import com.nwerl.lolstats.service.LeagueService;
import com.nwerl.lolstats.service.MatchService;
import com.nwerl.lolstats.service.SummonerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final LeagueService leagueService;
    private final MatchService matchService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("leagueList", leagueService.getLeagueRanking());
        return "index";
    }

    @GetMapping("/summoner/{summonerName}/matchList")
    public String summonerMatchList(Model model, @PathVariable String summonerName) {
        model.addAttribute("matchList", matchService);
        return "index";
    }
}