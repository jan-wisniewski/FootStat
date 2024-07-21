package com.janwisniewski.domain.services;

import com.janwisniewski.domain.elems.Player;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private String findSurname(List<Node> nodes) {
        return nodes.stream()
                .filter(e -> e instanceof TextNode && !((TextNode) e).text().trim().isEmpty())
                .map(e -> ((TextNode) e).text())
                .findFirst()
                .orElse("");
    }

    private String findName(List<Node> nodes) {
        for (Node node : nodes) {
            if (node instanceof Element) {
                Element path = (Element) node;
                if (path.hasClass("imie")) {
                    return path.text();
                }
            }
        }
        return "";
    }

    public Player createPlayer(Element element) {
        List<Node> nodes = element.childNodes();
        String name = findName(nodes);
        String surname = findSurname(nodes);
        return Player.builder().firstName(name.trim()).lastName(surname.trim()).build();
    }

}
