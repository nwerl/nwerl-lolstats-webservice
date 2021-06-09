package com.nwerl.lolstats.service.league;

import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.league.LeagueItemRepository;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueListDto;
import com.nwerl.lolstats.web.dto.view.LeagueRankingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public final static int CHALLENGER_LIMIT = 300;

    public Page<RiotLeagueItemDto> findAllChallengerLeagueItem(Pageable pageable) {
        Page<LeagueItem> leagueItemPage = leagueItemRepository.findAll(pageable);
        int totalElements = (int)leagueItemPage.getTotalElements();

        return new PageImpl<>(leagueItemPage.stream().map(LeagueItem::of).collect(Collectors.toList()), pageable, totalElements);
    }

    public Page<RiotLeagueItemDto> findAllChallengerLeagueItem() {
        return findAllChallengerLeagueItem(PageRequest.of(0, CHALLENGER_LIMIT));
    }

    public RiotLeagueListDto fetchChallengerLeagueListFromRiotApi() {
        return leagueApiCaller.fetchChallengerLeagueListFromRiotApi();
    }

    @Cacheable(value = "ranking")
    public List<LeagueRankingDto> getLeagueRanking() {
        AtomicInteger rank = new AtomicInteger(1);
        Page<LeagueItem> ranking = leagueItemRepository.findAllByOrderByLeaguePointsDesc(PageRequest.of(0, CHALLENGER_LIMIT));
        List<LeagueRankingDto> leagueRankingDtos = new ArrayList<>();

        for(LeagueItem item : ranking) {
            leagueRankingDtos.add(new LeagueRankingDto(rank.getAndIncrement(), item.getSummonerName(), item.getLeaguePoints()));
        }

        return leagueRankingDtos;
    }
}