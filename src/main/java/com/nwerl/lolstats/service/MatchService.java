package com.nwerl.lolstats.service;


import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.domain.match.MatchReference;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.domain.match.MatchReferenceRepository;
import com.nwerl.lolstats.web.dto.MatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
    private final MatchReferenceRepository matchReferenceRepository;

    public List<MatchReferenceDto> updateMatchReferenceByName(String name) {
        log.info("Update MatchReferences");
        return matchReferenceRepository.saveAll(riotApiRequestService.getMatchReferencesByAccountId(summonerService.findByName(name).getAccountId()).stream()
                    .map(MatchReferenceDto::toEntity)
                    .collect(Collectors.toList())).stream()
                                                .map(MatchReference::toDto)
                                                .collect(Collectors.toList());
    }

    public Boolean updateMatchByName(String name) {
        log.info("Update Matches");
        List<MatchReferenceDto> matchReferenceDtos = updateMatchReferenceByName(name);
        List<Match> matchList = new ArrayList<>();

        try {
            for (int i = matchReferenceDtos.size(); i-- > 0; ) {
                MatchReferenceDto matchReferenceDto = matchReferenceDtos.get(i);
                if (!matchRepository.existsByGameId(matchReferenceDto.getGameId()))
                    matchList.add(riotApiRequestService.getMatchByGameId(matchReferenceDto.getGameId()));
            }
        }
        catch(HttpClientErrorException.TooManyRequests e) {
            throw e;
        }
        finally {
            matchRepository.saveAll(matchList);
            log.info("Update Match count: {}", matchList.size());
        }

        return true;
    }
}
