package com.nwerl.lolstats.service.summoner;

import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.riotapi.summoner.RiotSummonerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SummonerService {
    private final SummonerApiCaller summonerApiCaller;
    private final SummonerRepository summonerRepository;

    public String findAccountIdByName(String name) {
        return summonerRepository.findByName(name).getAccountId();
    }

    public String findAccountIdBySummonerId(String id) {
        return summonerRepository.findBySummonerId(id).map(Summoner::getAccountId).orElse("");
    }

    public List<String> findAccountIdListByIdList(List<String> summonerIdList) {
        List<String> accountIdList = new ArrayList<>();

        for(String id : summonerIdList) {
            accountIdList.add(findAccountIdBySummonerId(id));
        }

        return accountIdList;
    }

    public RiotSummonerDto fetchSummonerFromRiotApiByName(String name) {
        return summonerApiCaller.fetchSummonerFromRiotApiByName(name);
    }

    public RiotSummonerDto fetchSummonerFromRiotApiById(String id) {
        return summonerApiCaller.fetchSummonerFromRiotApiById(id);
    }

    public Boolean checkByName(String name, String summonerId) {
        if(!summonerRepository.existsBySummonerId(summonerId)) {
            return false;
        }
        else {
            Summoner summoner = summonerRepository.findBySummonerId(summonerId).get();
            if (!summoner.getName().equals(name)) {
                //닉네임 변경 시
                summoner.modifyName(name);
                summonerRepository.save(summoner);
            }

            return true;
        }
    }
}