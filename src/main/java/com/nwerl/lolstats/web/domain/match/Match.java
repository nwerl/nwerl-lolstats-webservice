package com.nwerl.lolstats.web.domain.match;

import com.nwerl.lolstats.web.dto.view.MatchDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "_match")
public class Match {
    @Id
    private Long id;

    private String gameType;
    private Integer mapId;
    private String gameMode;

    private Integer seasonId;

    private Long gameDuration;
    private Long gameCreation;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "player", joinColumns = @JoinColumn(name = "match_id", referencedColumnName = "id"))
    @Column(name = "players")
    private List<Player> players;

    @Builder
    public Match(Long id, String gameType, Integer mapId, String gameMode, Integer seasonId, Long gameDuration, Long gameCreation, List<Player> players) {
        this.id = id;
        this.gameType = gameType;
        this.mapId = mapId;
        this.gameMode = gameMode;
        this.seasonId = seasonId;
        this.gameDuration = gameDuration;
        this.gameCreation = gameCreation;
        this.players = players;
    }

    public MatchDto of(String accountId) {
        Player owner = this.players.stream().filter(p -> p.getAccountId().equals(accountId)).findFirst().orElse(Player.builder().build());
        return MatchDto.builder()
                .gameCreation(gameCreation)
                .gameDuration(gameDuration)
                .gameId(id)
                .gameMode(gameMode)
                .gameType(gameType)
                .mapId(mapId)
                .seasonId(seasonId)
                .owner(owner.of())
                .players(players.stream().map(Player::of).collect(Collectors.toList()))
                .build();
    }
}