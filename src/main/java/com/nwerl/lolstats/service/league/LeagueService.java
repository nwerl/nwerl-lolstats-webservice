package com.nwerl.lolstats.service.league;

import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.league.LeagueItemRepository;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueListDto;
import com.nwerl.lolstats.web.dto.view.LeagueRankingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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

    public final static int RANKING_LIMIT = 300;

    public Page<RiotLeagueItemDto> findAll(Pageable pageable) {
        Page<LeagueItem> leagueItemPage = leagueItemRepository.findAll(pageable);
        int totalElements = (int)leagueItemPage.getTotalElements();

        return new PageImpl<>(leagueItemPage.stream().map(LeagueItem::of).collect(Collectors.toList()), pageable, totalElements);
    }

    public RiotLeagueListDto findAll() {
        return new RiotLeagueListDto(leagueItemRepository.findAll().stream().map(LeagueItem::of).collect(Collectors.toList()));
    }

    public void deleteInBatchBySummonerId(List<RiotLeagueItemDto> entities) {
        leagueItemRepository.deleteInBatchBySummonerId(entities.stream().map(RiotLeagueItemDto::toEntity).collect(Collectors.toList()));
    }

    public void deleteBySummonerId(String summonerId) {
        leagueItemRepository.deleteBySummonerId(summonerId);
    }


    public RiotLeagueListDto fetchChallengerLeagueListFromRiotApi() {
        return leagueApiCaller.fetchChallengerLeagueListFromRiotApi();
    }

    @CacheEvict(value = "ranking")
    public void updateLeagueRanking() {
        AtomicInteger pageNum = new AtomicInteger(0);
        Page<RiotLeagueItemDto> leagueItemDto = findAll(PageRequest.of(pageNum.get(), RANKING_LIMIT));

        while(!leagueItemDto.isLast()) {
            deleteInBatchBySummonerId(leagueItemDto.getContent());
            leagueItemDto = findAll(PageRequest.of(pageNum.getAndIncrement(), RANKING_LIMIT));
        }
    }

    @Cacheable(value = "ranking")
    public List<LeagueRankingDto> getLeagueRanking() {
        AtomicInteger rank = new AtomicInteger(1);
        Page<LeagueItem> ranking = leagueItemRepository.findAllByOrderByLeaguePointsDesc(PageRequest.of(0, RANKING_LIMIT));
        List<LeagueRankingDto> leagueRankingDtos = new ArrayList<>();

        for(LeagueItem item : ranking) {
            leagueRankingDtos.add(new LeagueRankingDto(rank.getAndIncrement(), item.getSummonerName(), item.getLeaguePoints()));
        }

        return leagueRankingDtos;
    }
}