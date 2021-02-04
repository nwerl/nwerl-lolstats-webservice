package com.nwerl.lolstats.service;


import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.domain.match.MatchList;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.domain.match.MatchListRepository;
import com.nwerl.lolstats.web.dto.riotApi.match.MatchDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.MatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final RiotApiRequestService riotApiRequestService;
    private final SummonerService summonerService;
    private final MatchListRepository matchListRepository;

    public MatchReferenceDto getLastMatchReferenceByName(String name) {
        log.info("Update MatchReferences");
        return riotApiRequestService.getLastMatchReference(name);
    }

    public MatchDto updateMatchByName(String name) {
        log.info("Update Matches");
        MatchReferenceDto matchReferenceDto = getLastMatchReferenceByName(name);
        return riotApiRequestService.getMatchByGameId(matchReferenceDto.getGameId());
    }

    public Boolean existsByGameId(Long gameId) {
        return matchRepository.existsByGameId(gameId);
    }
}
