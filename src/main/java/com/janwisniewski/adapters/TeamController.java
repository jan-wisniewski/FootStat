package com.janwisniewski.adapters;

import com.janwisniewski.domain.apis.ParserApi;
import com.janwisniewski.domain.elems.Match;
import com.janwisniewski.domain.elems.Player;
import com.janwisniewski.domain.elems.TeamSquad;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
@RequestMapping("/api/team")
@CrossOrigin(origins = "http://localhost:4200/")
public class TeamController {

    private final ParserApi parserApi;

    @GetMapping("")
    TeamInfoDto getInfo(@RequestParam String teamName) {
        return parserApi.getInfo(teamName);
    }

//    @GetMapping("/players")
//    Map<String, Integer> getSurnameMap() {
//        List<SeasonClubMatchDto> zagłębieLubin = parserApi.getInfo("Zagłębie Lubin");
//        List<TeamSquad> list = zagłębieLubin.stream().map(SeasonClubMatchDto::getMatchList).filter(Objects::nonNull).flatMap(List::stream).map(Match::getHomePlayers).toList();
//        List<TeamSquad> list2 = zagłębieLubin.stream().map(SeasonClubMatchDto::getMatchList).filter(Objects::nonNull).flatMap(List::stream).map(Match::getVisitorPlayers).toList();
//        List<TeamSquad> list1 = Stream.concat(Stream.of(list), Stream.of(list2)).flatMap(List::stream).toList();
//
//        Map<String, Integer> surnameMap = createSurnameMap(list1);
//        return surnameMap;
//    }

    public static Map<String, Integer> createSurnameMap(List<TeamSquad> teamSquads) {
        Map<String, Integer> surnameMap = new HashMap<>();


        for (TeamSquad teamSquad : teamSquads) {
            if (teamSquad != null) {
                for (Player player : teamSquad.getFirst11()) {
                    surnameMap.put(player.getLastName(), surnameMap.getOrDefault(player.getLastName(), 0) + 1);
                }
                for (Player player : teamSquad.getReserves()) {
                    surnameMap.put(player.getLastName(), surnameMap.getOrDefault(player.getLastName(), 0) + 1);
                }
            }
        }

        return surnameMap;
    }

}
