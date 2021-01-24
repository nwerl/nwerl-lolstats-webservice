package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.SummonerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SummonerService {
    private final SummonerRepository summonerRepository;
    private final RiotApiRequestService riotApiRequestService;

    public SummonerDto findByName(String name) {
        SummonerDto summonerDto;
        if(summonerRepository.existsByName(name)) {
            summonerDto = new SummonerDto(summonerRepository.findByName(name));
        }
        else {
            summonerDto = new SummonerDto(summonerRepository.save(riotApiRequestService.getSummonerInfoByName(name).toEntity()));
        }

        return summonerDto;
    }

}