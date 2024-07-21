package com.janwisniewski.domain.services;

import com.janwisniewski.domain.elems.Player;
import com.janwisniewski.domain.elems.Team;
import com.janwisniewski.domain.elems.TeamSquad;
import com.janwisniewski.domain.ports.TeamStoragePort;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class TeamService {
    private final TeamStoragePort teamStoragePort;
    private final PlayerService playerService;

    public TeamSquad parsePlayers(Elements team) {
        List<Player> first = new ArrayList<>();
        List<Player> reserves = new ArrayList<>();
        for (int i = 0; i < team.getFirst().childNodes().size(); i++) {
            Node node = team.getFirst().childNodes().get(i);
            Element player = (Element) node;
            Player player1 = playerService.createPlayer(player);
            if (player.hasClass("s1")) {
                first.add(player1);
            }
            if (player.hasClass("s2")) {
                reserves.add(player1);
            }
        }
        return TeamSquad.builder().first11(first).reserves(reserves).build();
    }

    public Team createTeam(String name) {
        return Team.builder().teamName(name).build();
    }

}
