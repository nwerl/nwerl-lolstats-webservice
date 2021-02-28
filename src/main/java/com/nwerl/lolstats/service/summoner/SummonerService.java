package com.nwerl.lolstats.service.summoner;

import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SummonerService {
    private final SummonerRepository summonerRepository;

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
}