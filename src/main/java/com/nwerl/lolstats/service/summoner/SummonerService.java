package com.nwerl.lolstats.service.summoner;

import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.riotapi.summoner.RiotSummonerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SummonerService {
    private final SummonerApiCaller summonerApiCaller;
    private final SummonerRepository summonerRepository;

    public String findAccountIdByName(String name) {
        return summonerRepository.findByName(name).getAccountId();
    }

    public String findAccountIdById(String id) {
        return summonerRepository.findById(id).get().getAccountId();
    }

    public RiotSummonerDto fetchSummonerFromRiotApiByName(String name) {
        return summonerApiCaller.fetchSummonerFromRiotApiByName(name);
    }

    public RiotSummonerDto fetchSummonerFromRiotApiById(String id) {
        return summonerApiCaller.fetchSummonerFromRiotApiById(id);
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
}