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
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchListRepository matchListRepository;
    private final MatchApiCaller matchApiCaller;
    private final SummonerService summonerService;
    private final RedisTemplate<String, MatchDto> redisTemplate;

    private ListOperations<String, MatchDto> listOps;

    @PostConstruct
    public void init() {
        listOps = redisTemplate.opsForList();
    }

    public Boolean existsByGameId(Long gameId) {
        return matchRepository.existsByGameId(gameId);
    }

    public RiotMatchReferenceDto fetchLastRankMatchReferenceFromRiotApi(String accountId) {
       RiotMatchReferenceDto dto = matchApiCaller.fetchMatchListFromRiotApi(accountId).getMatches().stream()
               .filter(matchReference -> matchReference.getQueue() == QueueType.SOLO_RANK.getQueueCode())
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

    public MatchDto getMatchBySummonerName(String summonerName, Long id) {
        String accountId = summonerService.findAccountIdByName(summonerName);
        return matchRepository.findById(id).map(m -> m.of(accountId)).orElse(MatchDto.builder().build());
    }

    public MatchDto getMatchByAccountId(String accountId, Long id) {
        return matchRepository.findById(id).map(m -> m.of(accountId)).orElse(MatchDto.builder().build());
    }

    public Boolean existsBySummonerName(String name) {
        String accountId = summonerService.findAccountIdByName(name);
        return matchListRepository.existsByAccountId(name);
    }

    public List<MatchDto> getMatchesByName(String name) {
        String accountId = summonerService.findAccountIdByName(name);
        Long size = listOps.size(accountId);

        if(size == null || size == 0) {
            return setMatchCache(accountId);
        }

        return getMatchCache(accountId);
    }

    private List<MatchDto> setMatchCache(String accountId) {
        List<MatchListDto.MatchReferenceDto> matchReferences = matchListRepository.findByAccountId(accountId).of().getMatchReferences();

        List<MatchDto> list = new ArrayList<>();
        for(MatchListDto.MatchReferenceDto item : matchReferences) {
            list.add(getMatchByAccountId(accountId, item.getGameId()));
        }

        listOps.rightPushAll(accountId, list);

        return list;
    }

    private List<MatchDto> getMatchCache(String accountId) {
        Long size = listOps.size(accountId);

        if(size == null || size == 0)
            return Collections.emptyList();

        return listOps.range(accountId, 0, size);
    }
}