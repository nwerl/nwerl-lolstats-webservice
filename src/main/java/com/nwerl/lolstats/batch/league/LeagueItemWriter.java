package com.nwerl.lolstats.batch.league;

import com.nwerl.lolstats.service.league.LeagueService;
import com.nwerl.lolstats.web.domain.league.LeagueItem;
import com.nwerl.lolstats.web.domain.league.LeagueItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Configuration
public class LeagueItemWriter implements ItemWriter<LeagueItem> {
    private final LeagueItemRepository leagueItemRepository;

    private AtomicInteger pageNum = new AtomicInteger();

    @Transactional
    @Override
    public void write(List<? extends LeagueItem> items) {
        //chunk 단위를 언제나 CHALLENGER_LIMIT 단위로 처리한다.
        Page<LeagueItem> leagueItemList = Optional.of(leagueItemRepository.findAll(PageRequest.of(pageNum.get(), LeagueService.CHALLENGER_LIMIT)))
                .orElse(Page.empty());

        if(!leagueItemList.isEmpty())
            leagueItemRepository.deleteInBatch(leagueItemList.getContent());

        leagueItemRepository.saveAll(items);
        //트랜잭션이 수행된 후 pageNum++ 해 준다.
        pageNum.incrementAndGet();
    }
}