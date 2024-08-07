package com.janwisniewski.domain.services;

import com.janwisniewski.domain.elems.*;
import com.janwisniewski.domain.elems.types.MatchType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class StatsService {

    private static final String teamName = "Legia Warszawa";

    private List<Match> getMatchesBasedOnType(List<Match> matches, MatchType matchType) {
        switch (matchType) {
            case HOME -> {
                return matches.stream().filter(e -> e.getHomeTeam().getTeamName().equals(teamName)).toList();
            }
            case AWAY -> {
                return matches.stream().filter(e -> e.getVisitorTeam().getTeamName().equals(teamName)).toList();
            }
        }
        return new ArrayList<>();
    }

    private String generateMatchBalanceBasedOnTeam(List<Match> matches) {
        int[] stats = new int[3];
        matches.forEach(e -> {
            if (e.getMyTeamResultType() == 1) {
                int val = stats[0];
                stats[0] = val + 1;
            }
            if (e.getMyTeamResultType() == 0) {
                int val = stats[1];
                stats[1] = val + 1;
            }
            if (e.getMyTeamResultType() == 2) {
                int val = stats[2];
                stats[2] = val + 1;
            }
        });
        return Arrays.stream(stats).mapToObj(String::valueOf).collect(Collectors.joining("-"));
    }

    private String generateMatchBalanceBasedOnMatchType(List<Match> matches, MatchType matchType) {
        int[] stats = new int[3];
        if (matchType.equals(MatchType.HOME)) {
            matches.forEach(e -> {
                if (e.getResultType() == 1) {
                    int val = stats[0];
                    stats[0] = val + 1;
                }
                if (e.getResultType() == 0) {
                    int val = stats[1];
                    stats[1] = val + 1;
                }
                if (e.getResultType() == 2) {
                    int val = stats[2];
                    stats[2] = val + 1;
                }
            });
        } else {
            matches.forEach(e -> {
                if (e.getResultType() == 2) {
                    int val = stats[0];
                    stats[0] = val + 1;
                }
                if (e.getResultType() == 0) {
                    int val = stats[1];
                    stats[1] = val + 1;
                }
                if (e.getResultType() == 1) {
                    int val = stats[2];
                    stats[2] = val + 1;
                }
            });
        }
        return Arrays.stream(stats).mapToObj(String::valueOf).collect(Collectors.joining("-"));
    }

    private String calculateTotalGoals(List<Match> matches, Function<Match, Integer> goalsExtractor) {
        return matches.stream()
                .map(goalsExtractor)
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("0");
    }

    private static String calculateAverageGoals(List<Match> matches, Function<Match, Integer> goalsExtractor) {
        double totalGoals = matches.stream()
                .map(goalsExtractor)
                .mapToInt(Integer::intValue)
                .sum();
        BigDecimal averageGoals = BigDecimal.valueOf(totalGoals / matches.size());
        return String.valueOf(averageGoals.setScale(2, RoundingMode.UP));
    }

    public static <T> Map<String, Long> createGoalsStats(
            Stream<T> stream,
            Function<T, String> mapper) {
        return stream
                .map(mapper)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private String generateTotalGoalsBalance(String goalsScoredHome, String goalsScoredAway, String goalsLostHome, String goalsLostAway) {
        return Integer.sum(Integer.parseInt(goalsScoredHome), Integer.parseInt(goalsScoredAway))+ " - "+Integer.sum(Integer.parseInt(goalsLostAway), Integer.parseInt(goalsLostHome));
    }

    public Stats generateFromMatches(List<Match> matches) {
        List<Match> atHome = getMatchesBasedOnType(matches, MatchType.HOME);
        List<Match> atAway = getMatchesBasedOnType(matches, MatchType.AWAY);
        String homeBalance = generateMatchBalanceBasedOnMatchType(atHome, MatchType.HOME);
        String awayBalance = generateMatchBalanceBasedOnMatchType(atAway, MatchType.AWAY);
        String totalBalance = generateMatchBalanceBasedOnTeam(matches);
        String teamScoredAtHome = calculateTotalGoals(atHome, Match::getHomeGoals);
        String teamScoredAway = calculateTotalGoals(atAway, Match::getVisitorGoals);
        String teamLostHome = calculateTotalGoals(atHome, Match::getVisitorGoals);
        String teamLostAway = calculateTotalGoals(atAway, Match::getHomeGoals);
        Map<String, Long> homeResults = createGoalsStats(
                atHome.stream(),
                e -> e.getHomeGoals() + "-" + e.getVisitorGoals());
        Map<String, Long> awayResults = createGoalsStats(
                atAway.stream(),
                e -> e.getHomeGoals() + "-" + e.getVisitorGoals());

        return Stats.builder()
                .homeStats(homeBalance)
                .awayStats(awayBalance)
                .goalsHome(teamScoredAtHome+ " - " +teamLostHome)
                .goalsVisitor(teamScoredAway+ " - "+teamLostAway)
                .averageScoredAtHome(calculateAverageGoals(atHome, Match::getHomeGoals))
                .averageScoredAtAway(calculateAverageGoals(atAway, Match::getVisitorGoals))
                .averageLostAtHome(calculateAverageGoals(atHome, Match::getVisitorGoals))
                .averageLostAtAway(calculateAverageGoals(atAway, Match::getHomeGoals))
                .homeResults(homeResults)
                .awayResults(awayResults)
                .totalStats(totalBalance)
                .goalsTotal(generateTotalGoalsBalance(teamScoredAtHome,teamScoredAway,teamLostHome,teamLostAway))
                .bestScorers(createScorerStats(matches))
                .minutesScoredAtHome(getGoalMinutes(atHome, MatchType.HOME))
                .minutesLostAtHome(getGoalMinutes(atHome, MatchType.AWAY))
                .minutesLostAtAway(getGoalMinutes(atAway, MatchType.HOME))
                .minutesScoredAtAway(getGoalMinutes(atAway, MatchType.AWAY))
                .build();
    }

    private Map<Integer, Long> getGoalMinutes(List<Match> matches, MatchType matchType) {
        return matches.stream()
                .peek(e -> System.out.println("parsing match: "+e.getId()))
                .flatMap(match -> {
                    if (matchType == MatchType.HOME) {
                        return match.getHomeScorers().stream();
                    } else {
                        return match.getVisitorScorers().stream();
                    }
                })
                .map(GoalEvent::getMin)
                .sorted(Comparator.comparingInt(Integer::intValue))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
    }



    private Map<String, Long> createScorerStats(List<Match> matches) {
        Map<String, Long> scorerGoalsMap = new HashMap<>();

        for (Match match : matches) {
            for (GoalEvent goal : match.getHomeScorers()) {
                if (goal.getSurname() == null) {
                    String a = "";
                }
                scorerGoalsMap.put(goal.getSurname(), scorerGoalsMap.getOrDefault(goal.getSurname(), 0L) + 1);
            }

            for (GoalEvent goal : match.getVisitorScorers()) {
                if (goal.getSurname() == null) {
                    String a = "";
                }
                scorerGoalsMap.put(goal.getSurname(), scorerGoalsMap.getOrDefault(goal.getSurname(), 0L) + 1);
            }
        }

        return scorerGoalsMap;
    }

}
