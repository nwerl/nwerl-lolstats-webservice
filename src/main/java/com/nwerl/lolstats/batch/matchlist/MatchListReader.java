package com.nwerl.lolstats.batch.matchlist;

import com.nwerl.lolstats.service.league.LeagueService;
import com.nwerl.lolstats.service.match.MatchService;
import com.nwerl.lolstats.service.summoner.SummonerService;
import com.nwerl.lolstats.web.dto.riotapi.league.RiotLeagueItemDto;
import com.nwerl.lolstats.web.dto.riotapi.matchreference.RiotMatchReferenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MatchListReader implements ItemReader<RiotMatchReferenceDto> {
    private final MatchService matchService;
    private final LeagueService leagueService;
    private final SummonerService summonerService;

    private Queue<String> leagueItemAccountIdQueue;

    @Override
    public RiotMatchReferenceDto read() throws Exception{
        if(leagueItemAccountIdQueueIsNotInitialized()) {
            setLeagueItemAccountIdQueue();
        }

        String accountId =  leagueItemAccountIdQueue.poll();
        if(accountId == null)   return null;

        return getLastMatchReference(accountId);
    }

    private RiotMatchReferenceDto getLastMatchReference(String accountId) throws InterruptedException {
        return matchService.fetchLastRankMatchReferenceFromRiotApi(accountId);
    }

    private Boolean leagueItemAccountIdQueueIsNotInitialized() {
        return leagueItemAccountIdQueue == null;
    }

    private void setLeagueItemAccountIdQueue() {
        leagueItemAccountIdQueue = new LinkedList<>(summonerService.findAccountIdListByIdList(
                leagueService.findAll().getEntries().stream()
                .map(RiotLeagueItemDto::getSummonerId).collect(Collectors.toList())));
    }
}