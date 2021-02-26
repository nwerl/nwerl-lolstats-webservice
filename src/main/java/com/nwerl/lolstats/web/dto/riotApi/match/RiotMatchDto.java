package com.nwerl.lolstats.web.dto.riotApi.match;

import com.nwerl.lolstats.web.domain.match.Match;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.Id;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiotMatchDto {
    private Long gameId;
    private List<ParticipantIdentityDto> participantIdentities;
    private Integer queueId;
    private String gameType;
    private Long gameDuration;
    private List<TeamStatsDto> teams;
    private String platformId;
    private Long gameCreation;
    private Integer seasonId;
    private String gameVersion;
    private Integer mapId;
    private String gameMode;
    private List<ParticipantDto> participants;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantIdentityDto {
        private Integer participantId;
        private PlayerDto player;

        @Builder
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PlayerDto {
            private Integer profileIcon;
            private String accountId;
            private String matchHistoryUri;
            private String currentAccountId;
            private String currentPlatformId;
            private String summonerName;
            private String summonerId;
            private String platformId;
        }

    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamStatsDto {
        private Integer towerKills;
        private Integer riftHeraldKills;
        private Boolean firstBlood;
        private Integer inhibitorKills;
        private List<TeamBansDto> bans;
        private Boolean firstBaron;
        private Boolean firstDragon;
        private Integer dominionVictoryScore;
        private Integer dragonKills;
        private Integer baronKills;
        private Boolean firstInhibitor;
        private Boolean firstTower;
        private Integer vilemawKills;
        private Boolean firstRiftHerald;
        private Integer teamId;
        private String win;

        @Builder
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        static class TeamBansDto {
            private Integer championId;
            private Integer pickTurn;
        }
    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class ParticipantDto {
        private Integer participantId;
        private Integer championId;
        private List<RuneDto> runes;
        private ParticipantStatsDto stats;
        private Integer teamId;
        private ParticipantTimelineDto timeline;
        private Integer spell1Id;
        private Integer spell2Id;
        private String highestAchievedSeasonTier;
        private List<MasteryDto> masteries;

        @Builder
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        static class RuneDto {
            private Integer runeId;
            private Integer rank;
        }

        @Builder
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class MasteryDto {
            private Integer rank;
            private Integer masteryId;
        }

        @Builder
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ParticipantStatsDto {
            private Integer item0;
            private Integer item2;
            private Integer totalUnitsHealed;
            private Integer item1;
            private Integer largestMultiKill;
            private Integer goldEarned;
            private Boolean firstInhibitorKill;
            private Long physicalDamageTaken;
            private Integer nodeNeutralizeAssist;
            private Integer totalPlayerScore ;
            private Integer champLevel;
            private Long damageDealtToObjectives;
            private Long totalDamageTaken;
            private Integer neutralMinionsKilled;
            private Integer deaths;
            private Integer tripleKills;
            private Long magicDamageDealtToChampions;
            private Integer wardsKilled;
            private Integer pentaKills;
            private Long damageSelfMitigated;
            private Integer largestCriticalStrike;
            private Integer nodeNeutralize;
            private Integer totalTimeCrowdControlDealt;
            private Boolean firstTowerKill;
            private Long magicDamageDealt;
            private Integer totalScoreRank;
            private Integer nodeCapture;
            private Integer wardsPlaced;
            private Long totalDamageDealt;
            private Long timeCCingOthers;
            private Long magicalDamageTaken;
            private Integer largestKillingSpree;
            private Long totalDamageDealtToChampions;
            private Long physicalDamageDealtToChampions;
            private Integer neutralMinionsKilledTeamJungle;
            private Integer totalMinionsKilled;
            private Boolean firstInhibitorAssist;
            private Integer visionWardsBoughtInGame;
            private Integer objectivePlayerScore;
            private Integer kills;
            private Boolean firstTowerAssist;
            private Integer combatPlayerScore;
            private Integer inhibitorKills;
            private Integer turretKills;
            private Integer participantId;
            private Long trueDamageTaken;
            private Boolean firstBloodAssist;
            private Integer nodeCaptureAssist;
            private Integer assists;
            private Integer teamObjective;
            private Integer altarsNeutralized;
            private Integer goldSpent;
            private Long damageDealtToTurrets;
            private Integer altarsCaptured;
            private Boolean win;
            private Long totalHeal;
            private Integer unrealKills;
            private Long visionScore;
            private Long physicalDamageDealt;
            private Boolean firstBloodKill;
            private Integer longestTimeSpentLiving;
            private Integer killingSprees;
            private Integer sightWardsBoughtInGame;
            private Long trueDamageDealtToChampions;
            private Integer neutralMinionsKilledEnemyJungle;
            private Integer doubleKills;
            private Long trueDamageDealt;
            private Integer quadraKills;
            private Integer item4;
            private Integer item3;
            private Integer item6;
            private Integer item5;
            private Integer playerScore0;
            private Integer playerScore1;
            private Integer playerScore2;
            private Integer playerScore3;
            private Integer playerScore4;
            private Integer playerScore5;
            private Integer playerScore6;
            private Integer playerScore7;
            private Integer playerScore8;
            private Integer playerScore9;
            private Integer perk0;
            private Integer perk0Var1;
            private Integer perk0Var2;
            private Integer perk0Var3;
            private Integer perk1;
            private Integer perk1Var1;
            private Integer perk1Var2;
            private Integer perk1Var3;
            private Integer perk2;
            private Integer perk2Var1;
            private Integer perk2Var2;
            private Integer perk2Var3;
            private Integer perk3;
            private Integer perk3Var1;
            private Integer perk3Var2;
            private Integer perk3Var3;
            private Integer perk4;
            private Integer perk4Var1;
            private Integer perk4Var2;
            private Integer perk4Var3;
            private Integer perk5;
            private Integer perk5Var1;
            private Integer perk5Var2;
            private Integer perk5Var3;
            private Integer perkPrimaryStyle;
            private Integer perkSubStyle;
            private Integer statPerk0;
            private Integer statPerk1;
            private Integer statPerk2;
        }

        @Builder
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ParticipantTimelineDto {
            private Integer participantId;
            private Map<String, Double> csDiffPerMinDeltas;
            private Map<String, Double> damageTakenPerMinDeltas;
            private String role;
            private Map<String, Double> damageTakenDiffPerMinDeltas;
            private Map<String, Double> xpPerMinDeltas;
            private Map<String, Double> xpDiffPerMinDeltas;
            private String lane;
            private Map<String, Double> creepsPerMinDeltas;
            private Map<String, Double> goldPerMinDeltas;
        }
    }

    public Match toEntity() {
        List<Match.Player> list = new ArrayList<>();
        for(int i=0;i<10;i++) {
            list.add(Match.Player.builder()
                    .accountId(participantIdentities.get(i).player.getAccountId())
                    .summonerName(participantIdentities.get(i).player.getSummonerName())
                    .teamId(participants.get(i).getTeamId())
                    .win(participants.get(i).stats.getWin())
                    .championId(participants.get(i).getChampionId())
                    .champLevel(participants.get(i).stats.getChampLevel())
                    .kills(participants.get(i).stats.getKills())
                    .assists(participants.get(i).stats.getAssists())
                    .deaths(participants.get(i).stats.getDeaths())
                    .item0(participants.get(i).stats.getItem0())
                    .item1(participants.get(i).stats.getItem1())
                    .item2(participants.get(i).stats.getItem2())
                    .item3(participants.get(i).stats.getItem3())
                    .item4(participants.get(i).stats.getItem4())
                    .item5(participants.get(i).stats.getItem5())
                    .item6(participants.get(i).stats.getItem6())
                    .visionWardsBoughtInGame(participants.get(i).stats.getVisionWardsBoughtInGame())
                    .goldEarned(participants.get(i).stats.getGoldEarned())
                    .perkPrimaryStyle(participants.get(i).stats.getPerk0())
                    .perkSubStyle(participants.get(i).stats.getPerkSubStyle())
                    .spell1Id(participants.get(i).getSpell1Id())
                    .spell2Id(participants.get(i).getSpell2Id())
                    .totalDamageDealtToChampions(participants.get(i).stats.getTotalDamageDealtToChampions())
                    .totalMinionsKilled(participants.get(i).stats.getTotalMinionsKilled()).build());
        }

        return Match.builder()
                .gameId(gameId)
                .seasonId(seasonId)
                .mapId(mapId)
                .gameType(gameType)
                .gameMode(gameMode)
                .gameDuration(gameDuration)
                .gameCreation(gameCreation)
                .players(list)
                .build();
    }
}