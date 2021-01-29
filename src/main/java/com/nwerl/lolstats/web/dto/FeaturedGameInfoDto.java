package com.nwerl.lolstats.web.dto;

import com.nwerl.lolstats.web.domain.match.FeaturedGameInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class FeaturedGameInfoDto {
    @Id
    private Long gameId;
    private String gameMode;
    private Long gameLength;
    private Long mapId;
    private String gameType;
    private List<BannedChampionDto> bannedChampions;
    private Long gameQueueConfigId;
    private Long gameStartTime;
    private List<ParticipantDto> participants;
    private String platformId;

    public FeaturedGameInfo toEntity() {
        return FeaturedGameInfo.builder()
                .gameId(gameId)
                .gameLength(gameLength)
                .gameMode(gameMode)
                .gameQueueConfigId(gameQueueConfigId)
                .gameStartTime(gameStartTime)
                .gameType(gameType)
                .mapId(mapId)
                .platformId(platformId)
                .participants(this.participants.stream()
                                                .map(ParticipantDto::toEntity)
                                                .collect(Collectors.toList()))
                .bannedChampions(this.bannedChampions.stream()
                                                .map(BannedChampionDto::toEntity)
                                                .collect(Collectors.toList()))
                .build();
    }

    @Builder
    @Data
    public static class BannedChampionDto {
        private Integer pickTurn;
        private Long championId;
        private Long teamId;

        public FeaturedGameInfo.BannedChampion toEntity() {
            return FeaturedGameInfo.BannedChampion.builder()
                    .championId(championId)
                    .pickTurn(pickTurn)
                    .teamId(teamId)
                    .build();
        }
    }

    @Builder
    @Data
    public static class ParticipantDto {
        private Boolean bot;
        private Long spell2Id;
        private Long profileIconId;
        private String summonerName;
        private Long championId;
        private Long teamId;
        private Long spell1Id;

        public FeaturedGameInfo.Participant toEntity() {
            return FeaturedGameInfo.Participant.builder()
                    .bot(bot)
                    .spell1Id(spell1Id)
                    .spell2Id(spell2Id)
                    .profileIconId(profileIconId)
                    .summonerName(summonerName)
                    .championId(championId)
                    .teamId(teamId)
                    .build();
        }
    }
}
