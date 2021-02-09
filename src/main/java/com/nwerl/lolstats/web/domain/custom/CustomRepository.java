package com.nwerl.lolstats.web.domain.custom;

import com.nwerl.lolstats.web.domain.match.MatchReference;

public interface CustomRepository {
    public void customMethod(String accountId, MatchReference matchReference);
}
