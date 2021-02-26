package com.nwerl.lolstats.service.match;

import com.nwerl.lolstats.service.summoner.SummonerService;
import com.nwerl.lolstats.web.domain.match.MatchListRepository;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.dto.riotApi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.view.MatchDto;
import com.nwerl.lolstats.web.dto.view.MatchListDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.RiotMatchListDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.RiotMatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchListRepository matchListRepository;
    private final SummonerService summonerService;

    public Boolean existsByGameId(Long gameId) {
        return matchRepository.existsByGameId(gameId);
    }


    public MatchListDto getMatchListByName(String name) {
        String Id = summonerService.findAccountIdByName(name);

        return matchListRepository.findByAccountId(Id, 0, 20).of();
    }

    public MatchDto getMatchById(String name, Long id) {
        String accountId = summonerService.findAccountIdByName(name);
        return matchRepository.findById(id).get().of(accountId);
    }

    public List<MatchDto> getMatchesByName(String name) {
        List<MatchListDto.MatchReferenceDto> matchReferences = getMatchListByName(name).getMatchReferences();

        List<MatchDto> list = new ArrayList<>();
        for(MatchListDto.MatchReferenceDto item : matchReferences) {
            list.add(getMatchById(name, item.getGameId()));
        }

        return list;
    }
}