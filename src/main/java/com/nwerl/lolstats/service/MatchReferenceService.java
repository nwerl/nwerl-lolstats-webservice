package com.nwerl.lolstats.service;

import com.mongodb.MongoException;
import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.domain.match.MatchReference;
import com.nwerl.lolstats.web.domain.match.MatchReferenceRepository;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.dto.MatchReferenceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MatchReferenceService {
    private final MatchReferenceRepository matchReferenceRepository;
    private final MatchRepository matchRepository;
    private final RiotApiRequestService riotApiRequestService;
    private final SummonerService summonerService;

    public List<MatchReferenceDto> updateMatchReferenceByName(String name) {
       return matchReferenceRepository.saveAll(riotApiRequestService.getMatchReferencesByName(summonerService.findByName(name).getName()).stream()
                    .map(MatchReferenceDto::toEntity)
                    .collect(Collectors.toList())).stream()
                                                .map(MatchReference::toDto)
                                                .collect(Collectors.toList());
    }

    public Boolean updateMatchByName(String name) throws InterruptedException {
        List<MatchReferenceDto> matchReferenceDtos = updateMatchReferenceByName(name);
        for(MatchReferenceDto matchReferenceDto : matchReferenceDtos) {
            matchRepository.save(riotApiRequestService.getMatchByGameId(matchReferenceDto.getGameId()));
            Thread.sleep(1200);
        }

        return true;
    }
}
