package com.janwisniewski.domain.elems;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Match {
    private Team homeTeam;
    private Team visitorTeam;
    private int homeGoals;
    private int visitorGoals;
    private TeamSquad homePlayers;
    private TeamSquad visitorPlayers;
    private String id;
    private String desc;
    private boolean completed;
    private int resultType;
    private int myTeamResultType;
    private List<GoalEvent> homeScorers;
    private List<GoalEvent> visitorScorers;
}
