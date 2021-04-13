package com.nwerl.lolstats.service.match;

import com.nwerl.lolstats.service.summoner.SummonerService;
import com.nwerl.lolstats.web.domain.match.MatchReferenceRepository;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.QueueType;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
import com.nwerl.lolstats.web.dto.view.MatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchReferenceRepository matchReferenceRepository;
    private final MatchApiCaller matchApiCaller;
    private final SummonerService summonerService;
    private final RedisTemplate<String, MatchDto> redisTemplate;

    private ZSetOperations<String, MatchDto> zSetOps;

    public static final int CACHE_LIMIT = 1000;

    @PostConstruct
    public void init() {
        zSetOps = redisTemplate.opsForZSet();
    }

    public Boolean existsByGameId(Long gameId) {
        return matchRepository.existsById(gameId);
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

    public List<MatchDto> getMatchesByName(String name) {
        String accountId = summonerService.findAccountIdByName(name);
        Long size = zSetOps.size(accountId);

        if(size == null || size == 0) {
            setMatchCache(accountId);
        }

        return getMatchCache(accountId);
    }

    public void setMatchCache(String accountId) {
        List<MatchDto> list = matchReferenceRepository.findAllBySummoner_AccountId(accountId, PageRequest.of(0, CACHE_LIMIT))
                .stream().map(m -> m.getMatch().of(accountId)).collect(Collectors.toList());

        for(MatchDto dto : list)
            zSetOps.add(accountId, dto, dto.getGameId());
    }

    public List<MatchDto> getMatchCache(String accountId) {
        Long size = zSetOps.size(accountId);

        if(size == null || size == 0)
            return Collections.emptyList();

        return new ArrayList<>(zSetOps.reverseRange(accountId, 0, size));
    }
}