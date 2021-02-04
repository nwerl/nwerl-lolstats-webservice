package com.nwerl.lolstats.web.domain.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Document(collection = "match")
public class Match {
    @Id
    private Long gameId;

    private String gameType;
    private Integer mapId;
    private String gameMode;

    private Integer seasonId;

    private Long gameDuration;
    private Long gameCreation;


    private List<Player> players;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Player {
        @Id
        private String accountId;
        private String summonerName;

        private Integer teamId;
        private Boolean win;

        private Integer championId;
        private Integer spell1Id;
        private Integer spell2Id;
        private Integer perkPrimaryStyle;
        private Integer perkSubStyle;

        private Integer kills;
        private Integer deaths;
        private Integer assists;

        private Integer champLevel;
        private Integer totalMinionsKilled;

        public Integer item0;
        public Integer item1;
        public Integer item2;
        public Integer item3;
        public Integer item4;
        public Integer item5;
        public Integer item6;
        private Integer visionWardsBoughtInGame;

        private Long totalDamageDealtToChampions;
        private Integer goldEarned;
    }
}