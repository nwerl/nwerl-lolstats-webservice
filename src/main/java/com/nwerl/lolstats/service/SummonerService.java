package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.SummonerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class SummonerService {
    private final SummonerRepository summonerRepository;

    public String save(SummonerDTO summonerDTO) {
        return summonerRepository.save(summonerDTO.toEntity()).getId();
    }
}
