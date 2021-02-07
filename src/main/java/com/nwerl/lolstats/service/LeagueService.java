package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.league.LeagueItemRepository;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LeagueService {
    private final LeagueItemRepository leagueItemRepository;
    private final RestTemplate restTemplate;

    public LeagueListDto findAll() {
        return new LeagueListDto(leagueItemRepository.findAll().stream().map(LeagueItem::of).collect(Collectors.toList()));
    }

    //ChallengerLeagueList를 갱신함.
    public LeagueListDto updateChallengerLeagueList() {
        List<LeagueItemDto> list = callApiChallengerLeagueItem().getEntries();
        leagueItemRepository.deleteAll(); //deleteAll 안하려면 N^2로 대조하여 받아온 list에서 없는 DB 데이터 삭제해야 함.
        leagueItemRepository.saveAll(list.stream().map(LeagueItemDto::toEntity).collect(Collectors.toList()));

        return new LeagueListDto(list);
    }

    public LeagueListDto callApiChallengerLeagueItem() {
        log.info("Call RiotApi to Get ChallengerLeagueItem");
        String uri = UriComponentsBuilder.newInstance()
                .path("/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5")
                .build().toString();

        return restTemplate.getForObject(uri, LeagueListDto.class);
    }

    public List<LeagueItemDto> getChallengerLeagueItem() {
        return leagueItemRepository.findAllByOrderByLeaguePointsDesc().stream().map(LeagueItem::of).collect(Collectors.toList());
    }
}