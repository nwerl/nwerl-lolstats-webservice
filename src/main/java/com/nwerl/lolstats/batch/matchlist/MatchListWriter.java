package com.nwerl.lolstats.batch.matchlist;


import com.nwerl.lolstats.web.domain.match.MatchList;
import com.nwerl.lolstats.web.domain.match.MatchListRepository;
import com.nwerl.lolstats.web.domain.match.MatchReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@StepScope
@Component
public class MatchListWriter implements ItemWriter<MatchReference> {
    private final MatchListRepository matchListRepository;
    private final String accountId;
    private final String summonerName;

    @Autowired
    public MatchListWriter(@Value("#{jobParameters[accountId]}") String accountId,
                           @Value("#{jobParameters[summonerName]}") String summonerName,
                           MatchListRepository matchListRepository) {

        this.matchListRepository = matchListRepository;
        this.accountId = accountId;
        this.summonerName = summonerName;
    }

    @Override
    public void write(List<? extends MatchReference> items) throws Exception {
        if(!matchListRepository.existsByAccountId(accountId)) {
            matchListRepository.save(MatchList.builder()
                    .accountId(accountId)
                    .summonerName(summonerName)
                    .matchReferences((List<MatchReference>) items)
                    .build());
        }
        else {
            for(MatchReference item : items) {
                matchListRepository.matchListFindAndModify(accountId, item);
            }
        }
    }
}
