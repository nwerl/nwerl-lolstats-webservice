package com.nwerl.lolstats.service.match;

import com.nwerl.lolstats.service.summoner.SummonerService;
import com.nwerl.lolstats.web.domain.match.MatchListRepository;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.QueueType;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
import com.nwerl.lolstats.web.dto.view.MatchDto;
import com.nwerl.lolstats.web.dto.view.MatchListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchListRepository matchListRepository;
    private final MatchApiCaller matchApiCaller;
    private final SummonerService summonerService;

    public Boolean existsByGameId(Long gameId) {
        return matchRepository.existsByGameId(gameId);
    }

    public RiotMatchReferenceDto fetchLastRankMatchReferenceFromRiotApi(String accountId) {
       RiotMatchReferenceDto dto = matchApiCaller.fetchMatchListFromRiotApi(accountId).getMatches().stream()
               .filter(matchReference -> matchReference.getQueue().equals(QueueType.SOLO_RANK))
               .filter(matchReference -> !existsByGameId(matchReference.getGameId()))
               .findFirst().map(reference -> {reference.setAccountId(accountId); return reference;})
               .orElse(null);

       return dto;
    }

    public RiotMatchDto fetchMatchFromRiotApi(Long gameId) {
        return matchApiCaller.fetchMatchFromRiotApi(gameId);
    }

    public MatchListDto getMatchListByName(String name) {
        String id = summonerService.findAccountIdByName(name);

        return matchListRepository.findByAccountId(id).of();
    }

    public MatchDto getMatchById(String name, Long id) {
        String accountId = summonerService.findAccountIdByName(name);
        return matchRepository.findById(id).map(m -> m.of(accountId)).orElse(MatchDto.builder().build());
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