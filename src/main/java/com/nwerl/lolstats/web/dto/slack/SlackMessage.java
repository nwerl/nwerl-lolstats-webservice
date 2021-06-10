package com.nwerl.lolstats.web.dto.slack;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SlackMessage {
    private String text;

    public static final String RIOT_API_KEY_WAS_EXPIRED = "Riot API key was expired. Update your Key!";

    public static SlackMessage alarmRiotApiKey() {
        return SlackMessage.builder().text(RIOT_API_KEY_WAS_EXPIRED + " " + LocalDateTime.now().toString()).build();
    }

    @Builder
    public SlackMessage(String text) {
        this.text = text;
    }
}
