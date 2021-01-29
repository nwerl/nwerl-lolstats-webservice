package com.nwerl.lolstats.service;

import com.nwerl.lolstats.web.domain.match.FeaturedGameInfo;
import com.nwerl.lolstats.web.domain.match.FeaturedGameInfoRepository;
import com.nwerl.lolstats.web.dto.FeaturedGameInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FeaturedGameInfoService {
    private final FeaturedGameInfoRepository featuredGameInfoRepository;
    private final RiotApiRequestService riotApiRequestService;

    public List<FeaturedGameInfoDto> updateFeaturedGames() {
        log.info("Update FeaturedGames");
        return featuredGameInfoRepository.saveAll(riotApiRequestService.getFeaturedGameInfo().stream()
                    .map(FeaturedGameInfoDto::toEntity).collect(Collectors.toList()))
                .stream()
                .map(FeaturedGameInfo::toDto).collect(Collectors.toList());
    }
}
