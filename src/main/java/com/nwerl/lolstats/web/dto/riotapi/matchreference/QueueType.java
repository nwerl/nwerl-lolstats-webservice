package com.nwerl.lolstats.web.dto.riotapi.matchreference;

import lombok.Getter;

@Getter
public enum QueueType {
    SOLO_RANK(420),
    NORMAL(430),
    TEAM_RANK(440),
    ARAM(450);

    private int queueCode;

    private QueueType(int queueCode) {
        this.queueCode = queueCode;
    }
}
