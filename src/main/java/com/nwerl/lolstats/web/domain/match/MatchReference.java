package com.nwerl.lolstats.web.domain.match;


import com.nwerl.lolstats.web.domain.summoner.Summoner;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "match_reference")
public class MatchReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private Long gameCreation;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private Summoner summoner;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @Builder
    public MatchReference(Long gameCreation, Summoner summoner, Match match) {
        this.gameCreation = gameCreation;
        this.summoner = summoner;
        this.match = match;
    }
}
