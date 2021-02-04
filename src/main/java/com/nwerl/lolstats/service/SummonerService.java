package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SummonerService {
    private final SummonerRepository summonerRepository;
    private final RiotApiRequestService riotApiRequestService;

    public SummonerDto findByName(String name) {
        SummonerDto summonerDto;
        if(summonerRepository.existsByName(name)) {
            log.info("Requested Name exists in DB");
            summonerDto = new SummonerDto(summonerRepository.findByName(name));
        }
        else {
            log.info("Requested Name NOT exists in DB");
            summonerDto = new SummonerDto(summonerRepository.save(riotApiRequestService.getSummonerInfoByName(name).toEntity()));
        }
        return summonerDto;
    }

    public String findAccountIdByName(String name) {
        return summonerRepository.findByName(name).getAccountId();
    }

    public Boolean existsByName(String name) {
        return summonerRepository.existsByName(name);
    }
}