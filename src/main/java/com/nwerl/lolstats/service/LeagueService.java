package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.league.LeagueItemRepository;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueItemDto;
import com.nwerl.lolstats.web.dto.riotApi.league.LeagueListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LeagueService {
    private final LeagueItemRepository leagueItemRepository;
    private final RiotApiRequestService riotApiRequestService;
    private final SummonerService summonerService;

    public LeagueListDto findAll() {
        return new LeagueListDto(leagueItemRepository.findAll().stream().map(LeagueItem::of).collect(Collectors.toList()));
    }

    //ChallengerLeagueList를 갱신함.
    public LeagueListDto updateChallengerLeagueList() {
        List<LeagueItemDto> list = riotApiRequestService.getChallengerLeagueItem();
        leagueItemRepository.deleteAll(); //deleteAll 안하려면 N^2로 대조하여 받아온 list에서 없는 DB 데이터 삭제해야 함.
        leagueItemRepository.saveAll(list.stream().map(LeagueItemDto::toEntity).collect(Collectors.toList()));

        return new LeagueListDto(list);
    }
}