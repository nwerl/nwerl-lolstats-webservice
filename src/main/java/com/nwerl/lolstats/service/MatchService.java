package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.match.MatchRepository;
import com.nwerl.lolstats.web.dto.riotApi.match.MatchDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.MatchListDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.MatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final RestTemplate restTemplate;

    public MatchReferenceDto getLastMatchReferenceByName(String name) {
        log.info("Update MatchReferences");
        return getLastMatchReference(name);
    }

    public MatchDto updateMatchByName(String name) {
        log.info("Update Matches");
        MatchReferenceDto matchReferenceDto = getLastMatchReferenceByName(name);
        return getMatchByGameId(matchReferenceDto.getGameId());
    }

    public Boolean existsByGameId(Long gameId) {
        return matchRepository.existsByGameId(gameId);
    }

    public MatchReferenceDto getLastMatchReference(String accountId) {
        log.info("Call RiotApi to Get MatchReferences");
        String uri = UriComponentsBuilder.newInstance()
                .path("/match/v4/matchlists/by-account/").path(accountId).queryParam("endIndex", "1")
                .build().toString();
        return restTemplate.getForObject(uri, MatchListDto.class).getMatches().get(0);
    }

    public MatchDto getMatchByGameId(Long gameId) {
        String uri = UriComponentsBuilder.newInstance()
                .path("/match/v4/matches/").path(String.valueOf(gameId))
                .build().toString();
        return restTemplate.getForObject(uri, MatchDto.class);
    }
}
