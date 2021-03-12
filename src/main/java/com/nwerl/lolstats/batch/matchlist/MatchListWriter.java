package com.nwerl.lolstats.batch.matchlist;


import com.nwerl.lolstats.web.domain.match.MatchList;
import com.nwerl.lolstats.web.domain.match.MatchListRepository;
import com.nwerl.lolstats.web.domain.match.MatchReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchListWriter implements ItemWriter<MatchList> {
    private final MatchListRepository matchListRepository;

    @Override
    public void write(List<? extends MatchList> items) throws Exception {
        for(MatchList item : items) {
            if (!matchListRepository.existsByAccountId(item.getAccountId())) {
                matchListRepository.save(item);
            }
            else {
                for (MatchReference matchReference : item.getMatchReferences()) {
                    matchListRepository.matchListFindAndModify(item.getAccountId(), matchReference);
                }
            }
        }
    }
}
