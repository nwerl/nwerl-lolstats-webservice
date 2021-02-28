package com.nwerl.lolstats.service.league;

import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.league.LeagueItemRepository;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueListDto;
import com.nwerl.lolstats.web.dto.view.LeagueRankingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LeagueService {
    private final LeagueItemRepository leagueItemRepository;
    private final LeagueApiCaller leagueApiCaller;

    public RiotLeagueListDto findAll() {
        return new RiotLeagueListDto(leagueItemRepository.findAll().stream().map(LeagueItem::of).collect(Collectors.toList()));
    }

    //ChallengerLeagueList를 갱신함.
    public RiotLeagueListDto updateChallengerLeagueList() {
        List<RiotLeagueItemDto> list = leagueApiCaller.fetchChallengerLeagueListFromRiotApi().getEntries();
        leagueItemRepository.deleteAll(); //deleteAll 안하려면 N^2로 대조하여 받아온 list에서 없는 DB 데이터 삭제해야 함.
        leagueItemRepository.saveAll(list.stream().map(RiotLeagueItemDto::toEntity).collect(Collectors.toList()));

        return new RiotLeagueListDto(list);
    }

    public RiotLeagueListDto fetchChallengerLeagueListFromRiotApi() {
        return leagueApiCaller.fetchChallengerLeagueListFromRiotApi();
    }


    public List<LeagueRankingDto> getLeagueRanking() {
        AtomicInteger ranking = new AtomicInteger(1);

        List<LeagueRankingDto> leagueRankingDtos = new ArrayList<>();
        for(LeagueItem item : leagueItemRepository.findAllByOrderByLeaguePointsDesc()) {
            leagueRankingDtos.add(new LeagueRankingDto(ranking.getAndIncrement(), item.getSummonerName(), item.getLeaguePoints()));
        }

        return leagueRankingDtos;
    }
}