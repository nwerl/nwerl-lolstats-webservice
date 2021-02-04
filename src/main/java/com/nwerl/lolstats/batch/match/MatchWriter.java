package com.nwerl.lolstats.batch.match;

import com.nwerl.lolstats.web.domain.match.Match;
import com.nwerl.lolstats.web.domain.match.MatchList;
import com.nwerl.lolstats.web.domain.match.MatchListRepository;
import com.nwerl.lolstats.web.domain.match.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class MatchWriter implements ItemWriter<Match> {
    private final MatchRepository matchRepository;

    @Override
    public void write(List<? extends Match> items) throws Exception {
        matchRepository.saveAll(items);
    }
}
