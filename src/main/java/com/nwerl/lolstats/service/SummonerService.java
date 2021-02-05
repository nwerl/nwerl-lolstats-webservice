package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.riotApi.summoner.SummonerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class SummonerService {
    private final SummonerRepository summonerRepository;
    private final UriComponentsBuilder uriComponentsBuilder;
    private final RestTemplate restTemplate;

    public String findAccountIdByName(String name) {
        return summonerRepository.findByName(name).getAccountId();
    }

    public Boolean existsByName(String name) {
        return summonerRepository.existsByName(name);
    }

    public SummonerDto getSummonerInfoByName(String name) {
        log.info("Call RiotApi to Get SummonerInfo");
        String uri = uriComponentsBuilder.path("/summoner/v4/summoners/by-name/")
                .path(name)
                .build().toString();

        return restTemplate.getForObject(uri, SummonerDto.class);
    }
}