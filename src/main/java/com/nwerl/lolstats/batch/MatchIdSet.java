package com.nwerl.lolstats.batch;

import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class MatchIdSet {
    private Set<Long> matchIdSet = new LinkedHashSet<>();

    public Long getNextMatchId() {
        Iterator<Long> it = matchIdSet.iterator();

        if(!it.hasNext())   return null;
        Long nextMatchId = it.next();
        it.remove();

        return nextMatchId;
    }

    public void addMatchId(Long matchId) {
        matchIdSet.add(matchId);
    }

    public boolean noMatchesToUpdate() {
        return matchIdSet.isEmpty();
    }

    public int getSize() {
        return matchIdSet.size();
    }
}
