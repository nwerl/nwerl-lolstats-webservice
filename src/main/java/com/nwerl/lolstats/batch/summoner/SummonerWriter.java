package com.nwerl.lolstats.batch.summoner;

import com.nwerl.lolstats.web.domain.summoner.Summoner;
import com.nwerl.lolstats.web.domain.summoner.SummonerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SummonerWriter implements ItemWriter<Summoner> {
    private final SummonerRepository summonerRepository;

    @Override
    public void write(List<? extends Summoner> items) {
        summonerRepository.saveAll(items);
    }
}
