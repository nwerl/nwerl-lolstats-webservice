package com.nwerl.lolstats.service;

import com.mongodb.MongoException;
import com.nwerl.lolstats.web.domain.match.MatchReferenceRepository;
import com.nwerl.lolstats.web.dto.MatchReferenceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MatchReferenceService {
    private final MatchReferenceRepository matchReferenceRepository;
    private final RiotApiRequestService riotApiRequestService;
    private final SummonerService summonerService;

    public Boolean updateMatchReferenceByName(String name) {
        try {
            matchReferenceRepository.saveAll(riotApiRequestService.getMatchReferencesByName(summonerService.findByName(name).getName()).stream()
                    .map(MatchReferenceDto::toEntity)
                    .collect(Collectors.toList()));
        }
        catch (MongoException e) {
            return false;
        }
        return true;
    }
}
