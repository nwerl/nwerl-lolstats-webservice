package com.nwerl.lolstats.service;


import com.nwerl.lolstats.web.domain.match.MatchReference;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.domain.match.MatchReferenceRepository;
import com.nwerl.lolstats.web.dto.MatchReferenceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final RiotApiRequestService riotApiRequestService;
    private final SummonerService summonerService;
    private final MatchReferenceRepository matchReferenceRepository;

    public List<MatchReferenceDto> updateMatchReferenceByName(String name) {
        return matchReferenceRepository.saveAll(riotApiRequestService.getMatchReferencesByAccountId(summonerService.findByName(name).getAccountId()).stream()
                    .map(MatchReferenceDto::toEntity)
                    .collect(Collectors.toList())).stream()
                                                .map(MatchReference::toDto)
                                                .collect(Collectors.toList());
    }

    public Boolean updateMatchByName(String name) {
        List<MatchReferenceDto> matchReferenceDtos = updateMatchReferenceByName(name);
        for (int i = matchReferenceDtos.size(); i-- > 0; ) {
            MatchReferenceDto matchReferenceDto = matchReferenceDtos.get(i);
            if(!matchRepository.existsByGameId(matchReferenceDto.getGameId()))
                matchRepository.save(riotApiRequestService.getMatchByGameId(matchReferenceDto.getGameId()));
        }

        return true;
    }
}
