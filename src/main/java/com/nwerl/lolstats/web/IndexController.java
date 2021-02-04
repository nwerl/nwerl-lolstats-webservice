package com.nwerl.lolstats.web;

import com.nwerl.lolstats.service.SummonerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final SummonerService summonerService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}
