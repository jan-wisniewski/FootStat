package com.janwisniewski.domain.elems;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Stats {
    private String homeStats;
    private String awayStats;
    private String totalStats;
    private String goalsHome;
    private String goalsVisitor;
    private String goalsTotal;
    private String averageScoredAtHome;
    private String averageScoredAtAway;
    private String averageLostAtHome;
    private String averageLostAtAway;
    private Map<String, Long> homeResults;
    private Map<String, Long> awayResults;
    private Map<String, Long> bestScorers;
    private Map<Integer, Long> minutesScoredAtHome;
    private Map<Integer, Long> minutesLostAtHome;
    private Map<Integer, Long> minutesLostAtAway;
    private Map<Integer, Long> minutesScoredAtAway;


}
