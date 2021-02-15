package com.nwerl.lolstats.service;

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
    private final RestTemplate restTemplate;

    public Boolean existsByGameId(Long gameId) {
        return matchRepository.existsByGameId(gameId);
    }

    public RiotMatchReferenceDto callApiLastMatchReference(String accountId) {
        log.info("Call RiotApi to Get MatchReferences");
        String uri = UriComponentsBuilder.newInstance()
                .path("/match/v4/matchlists/by-account/").path(accountId).queryParam("endIndex", "1")
                .build().toString();
        return restTemplate.getForObject(uri, RiotMatchListDto.class).getMatches().get(0);
    }

    public RiotMatchDto callApiMatchByGameId(Long gameId) {
        String uri = UriComponentsBuilder.newInstance()
                .path("/match/v4/matches/").path(String.valueOf(gameId))
                .build().toString();
        return restTemplate.getForObject(uri, RiotMatchDto.class);
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