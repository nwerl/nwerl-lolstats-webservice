package com.nwerl.lolstats.batch.match;


import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.web.domain.match.*;
import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import com.nwerl.lolstats.web.dto.view.MatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchWriter implements ItemWriter<Match> {
    private final RedisTemplate<String, MatchDto> redisTemplate;
    private final MatchService matchService;
    private final MatchRepository matchRepository;
    private final MatchReferenceRepository matchReferenceRepository;
    private final SummonerRepository summonerRepository;

    @Override
    public void write(List<? extends Match> items) {
        ZSetOperations<String, MatchDto> zSetOps = redisTemplate.opsForZSet();

        for(Match item : items) {
            matchRepository.save(item);

            for(Player player : item.getPlayers()) {
                String accountId = player.getAccountId();

                //DB에 없는 사용자일 경우, 패스한다
                if(!summonerRepository.existsByAccountId(accountId))
                    continue;

                Summoner summoner = summonerRepository.findByAccountId(accountId);
                matchReferenceRepository.save(MatchReference.builder().match(item).summoner(summoner).build());

                //DB에는 있으나 캐시에 데이터가 존재하지 않을 경우, 캐시에 올려 준다
                if(zSetOps.size(accountId) == 0)
                    matchService.setMatchCache(accountId);
                else
                    zSetOps.add(accountId, item.of(accountId), item.getId());
            }
        }
    }
}
