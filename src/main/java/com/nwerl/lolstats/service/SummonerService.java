package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.summoner.Summoner;
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
    private final RestTemplate restTemplate;

    public String findSummonerIdByName(String name) {
        return summonerRepository.findByName(name).getId();
    }

    public String findAccountIdByName(String name) {
        return summonerRepository.findByName(name).getAccountId();
    }

    public String findAccountIdById(String id) {
        return summonerRepository.findById(id).get().getAccountId();
    }

    public Boolean checkByName(String name, String summonerId) {
        if(!summonerRepository.existsById(summonerId)) {
            return false;
        }
        else {
            Summoner summoner = summonerRepository.findById(summonerId).get();
            if (!summoner.getName().equals(name)) {
                //닉네임 변경 시
                summoner.modifyName(name);
                summonerRepository.save(summoner);
            }

            return true;
        }
    }

    public SummonerDto callApiSummonerInfoBySummonerId(String id) {
        log.info("Call RiotApi to Get SummonerInfo : {}", id);
        String uri = UriComponentsBuilder.newInstance().path("/summoner/v4/summoners/")
                .path(id)
                .build().toString();
        return restTemplate.getForObject(uri, SummonerDto.class);
    }

    public SummonerDto callApiSummonerInfoByName(String name) {
        log.info("Call RiotApi to Get SummonerInfo : {}", name);
        String uri = UriComponentsBuilder.newInstance().path("/summoner/v4/summoners/by-name/")
                .path(name)
                .build().toString();
        return restTemplate.getForObject(uri, SummonerDto.class);
    }
}