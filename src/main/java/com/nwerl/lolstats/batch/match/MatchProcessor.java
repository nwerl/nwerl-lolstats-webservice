package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.dto.riotApi.match.MatchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
public class MatchProcessor implements ItemProcessor<MatchDto, Match> {

    @Override
    public Match process(MatchDto item) throws Exception {
        return item.toEntity();
    }
}
