package com.janwisniewski.domain.elems;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Match {
    private Team homeTeam;
    private Team visitorTeam;
    private int homeGoals;
    private int visitorGoals;
    private TeamSquad homePLayers;
    private TeamSquad visitorPlayers;
    private String id;
    private String desc;
}
