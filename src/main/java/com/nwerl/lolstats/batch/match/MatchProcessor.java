package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.dto.riotapi.match.RiotMatchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
public class MatchProcessor implements ItemProcessor<RiotMatchDto, Match> {

    @Override
    public Match process(RiotMatchDto item) throws Exception {
        //Thread.sleep(1400);
        return item.toEntity();
    }
}
