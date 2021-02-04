package com.nwerl.lolstats.web.domain.featuredgame;

import com.nwerl.lolstats.web.dto.riotApi.featuredgame.FeaturedGameInfoDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@Document(collection = "featuredGameInfo")
public class FeaturedGameInfo {
    @Id
    private Long gameId;
    private String gameMode;
    private Long gameLength;
    private Long mapId;
    private String gameType;
    private List<BannedChampion> bannedChampions;
    private Long gameQueueConfigId;
    private Long gameStartTime;
    private List<Participant> participants;
    private String platformId;

    public FeaturedGameInfoDto toDto() {
        return FeaturedGameInfoDto.builder()
                .gameId(gameId)
                .gameLength(gameLength)
                .gameMode(gameMode)
                .gameQueueConfigId(gameQueueConfigId)
                .gameStartTime(gameStartTime)
                .gameType(gameType)
                .mapId(mapId)
                .platformId(platformId)
                .participants(this.participants.stream()
                        .map(Participant::toDto)
                        .collect(Collectors.toList()))
                .bannedChampions(this.bannedChampions.stream()
                        .map(BannedChampion::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Builder
    @Data
    public static class BannedChampion {
        private Integer pickTurn;
        private Long championId;
        private Long teamId;

        public FeaturedGameInfoDto.BannedChampionDto toDto() {
            return FeaturedGameInfoDto.BannedChampionDto.builder()
                    .championId(championId)
                    .pickTurn(pickTurn)
                    .teamId(teamId)
                    .build();
        }
    }

    @Builder
    @Data
    public static class Participant {
        private Boolean bot;
        private Long spell2Id;
        private Long profileIconId;
        private String summonerName;
        private Long championId;
        private Long teamId;
        private Long spell1Id;

        public FeaturedGameInfoDto.ParticipantDto toDto() {
            return FeaturedGameInfoDto.ParticipantDto.builder()
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
