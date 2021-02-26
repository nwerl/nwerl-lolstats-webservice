package com.nwerl.lolstats.service.match;

import com.nwerl.lolstats.web.dto.riotApi.match.RiotMatchDto;
import com.nwerl.lolstats.web.dto.riotApi.matchreference.RiotMatchReferenceDto;

public interface MatchApiCaller {
    RiotMatchReferenceDto callApiLastMatchReference(String accountId);
    RiotMatchDto callApiMatchByGameId(Long gameId);
}