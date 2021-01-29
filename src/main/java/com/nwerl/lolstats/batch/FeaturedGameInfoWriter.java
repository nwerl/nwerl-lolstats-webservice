package com.nwerl.lolstats.batch;

import com.nwerl.lolstats.web.domain.match.FeaturedGameInfo;
import com.nwerl.lolstats.web.domain.match.FeaturedGameInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FeaturedGameInfoWriter implements ItemWriter<List<FeaturedGameInfo>> {
    private final FeaturedGameInfoRepository featuredGameInfoRepository;

    @Override
    public void write(List<? extends List<FeaturedGameInfo>> items) throws Exception {
        for(List<FeaturedGameInfo> item : items) {
            featuredGameInfoRepository.saveAll(item);
        }
    }
}
