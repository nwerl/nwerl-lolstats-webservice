package com.nwerl.lolstats.batch;

import com.nwerl.lolstats.web.domain.match.FeaturedGameInfo;
import com.nwerl.lolstats.web.dto.FeaturedGameInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@StepScope
@Configuration
public class FeaturedGameInfoProcessor implements ItemProcessor<List<FeaturedGameInfoDto>, List<FeaturedGameInfo>> {
    @Override
    public List<FeaturedGameInfo> process(List<FeaturedGameInfoDto> item) throws Exception {
        return item.stream().map(FeaturedGameInfoDto::toEntity).collect(Collectors.toList());
    }
}
