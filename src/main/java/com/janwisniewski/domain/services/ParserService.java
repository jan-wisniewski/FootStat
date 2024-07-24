package com.janwisniewski.domain.services;

import com.janwisniewski.adapters.PlayerDto;
import com.janwisniewski.adapters.SeasonClubMatchDto;
import com.janwisniewski.domain.apis.ParserApi;
import com.janwisniewski.domain.elems.Match;
import com.janwisniewski.domain.elems.Season;
import com.janwisniewski.domain.elems.Team;
import com.janwisniewski.domain.elems.TeamSquad;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class ParserService implements ParserApi {

    private final WebCrawlerService webCrawlerService;
    private final JsoupUtils jsoupUtils;
    private final TeamService teamService;
    private final MapperService mapperService;

    private final String LEGIA_URL = "https://legionisci.com/relacje";
    private final String MATCH_PREFIX = "https://legionisci.com/mecz/";

    public List<SeasonClubMatchDto> getInfo(String clubName) {
        String htmlContent = webCrawlerService.getHtmlContent(LEGIA_URL);
        Elements seasonSections = webCrawlerService.getHtmlElements(htmlContent, "div.terminarz");
        return parseSeasons(seasonSections, clubName);
    }


    @Override
    public List<PlayerDto> getPlayers() {
        return mapperService.parsePlayers();
    }

    private String getMatchId(String text) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private int getHomeGoals(String text) {
        return Integer.parseInt(text.split("-")[0]);
    }

    private int getVisitorGoals(String text) {
        return Integer.parseInt(text.split("-")[1]);
    }

    private TeamSquad getTeam(TeamType teamType, String matchId) {
        String html = webCrawlerService.getHtmlContent(MATCH_PREFIX + matchId);
        String selector = "";
        switch (teamType) {
            case HOME -> selector = "div.sklad_gospodarz";
            case VISITOR -> selector = "div.sklad_gosc";
            default -> throw new IllegalArgumentException("Incorrect team type");
        }
        return teamService.parsePlayers(webCrawlerService.getHtmlElements(html, selector));
    }

    private Match getInfoAfterMatch(Element element) {
        List<Node> nodes = element.childNodes();
        Node node = nodes.get(1);
        List<Node> nodes1 = node.childNodes();
        if (nodes1.isEmpty()) {
            nodes1 = nodes.get(0).childNodes();
        }
        String visitor = "";
        if (nodes1.size() > 3) {
            visitor = jsoupUtils.getTextFromNode(nodes1.get(3));
        } else {
            visitor = jsoupUtils.getTextFromNode(nodes1.get(2));
        }
        String matchId = getMatchId(((Element) nodes1.get(1).parentNode()).attributes().getIgnoreCase("href"));
        return Match.builder()
                .homeTeam(Team.builder().teamName(jsoupUtils.getTextFromNode(nodes1.get(0))).build())
                .homeGoals(getHomeGoals(jsoupUtils.getTextFromNode(nodes1.get(1))))
                .visitorGoals(getVisitorGoals(jsoupUtils.getTextFromNode(nodes1.get(1))))
                .id(matchId)
                .visitorTeam(Team.builder().teamName(visitor).build())
                .homePlayers(getTeam(TeamType.HOME, matchId))
                .visitorPlayers(getTeam(TeamType.VISITOR, matchId))
                .build();
    }

    private boolean isMatchEndNew(Node node) {
        boolean differentType = false;
        Node node1 = node.childNodes().get(1);
        if (node1 instanceof TextNode) {
            node1 = node.childNodes().get(0);
            differentType = true;
        }
        List<Node> nodes = node1.childNodes();
        boolean nodeContainsText;
        Node node11;
        if (differentType) {
            node11 = nodes.get(1);
            List<Node> childNodes = jsoupUtils.getChildNodes(node11);
            nodeContainsText = jsoupUtils.isNodeContainsResult(childNodes.getFirst());
        } else {
            node11 = nodes.getFirst();
            TextNode tn = (TextNode) node11;
            if (tn.text().trim().length() > 1) {
                Node node2 = nodes.get(1).childNodes().getFirst();
                nodeContainsText = jsoupUtils.isNodeContainsResult(node2);
            } else {
                nodeContainsText = jsoupUtils.isNodeContainsResult(node11);
            }
        }
        return nodeContainsText;
    }

    private Match parseMatch(Node node) {
        List<Node> childNodes = jsoupUtils.getChildNodes(node);
        Boolean matchEnded = isMatchEndNew(node);
        if (matchEnded) {
            return getInfoAfterMatch((Element) node);
        } else {
            return Match.builder()
                    .homeTeam(teamService.createTeam(jsoupUtils.getTextFromNode(childNodes.get(0))))
                    .visitorTeam(teamService.createTeam(jsoupUtils.getTextFromNode(childNodes.get(2))))
                    .build();
        }
    }

    private List<SeasonClubMatchDto> parseSeasons(Elements seasons, String clubName) {
        List<SeasonClubMatchDto> seasonClubMatchDtoList = new ArrayList<>();
        for (Element season : seasons) {
            List<Match> matches;
            SeasonClubMatchDto seasonClubMatchDto = new SeasonClubMatchDto();
            seasonClubMatchDto.setSeason(createSeason(season));
            matches = parseSeason(season, clubName);
            seasonClubMatchDto.setMatchList(matches);
            if (!matches.isEmpty()) {
                seasonClubMatchDtoList.add(seasonClubMatchDto);
            }
        }
        return seasonClubMatchDtoList;
    }

    private Season createSeason(Element season) {
        Node node = ((Element) ((Element) season.childNodes().get(2)).childNodes().get(1)).childNodes().get(0);
        TextNode last = (TextNode) node;
        return new Season(last.text());
    }

    private List<String> getTeamNameFromMatch(Node match) {
        List<Node> childNodes = jsoupUtils.getChildNodes(match);
        List<String> res = new ArrayList<>();
        for (Node node : childNodes) {
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.hasClass("mecz")) {
                    List<Node> childNodes1 = jsoupUtils.getChildNodes(element);
                    for (Node node1 : childNodes1) {
                        //mecz nieodbyty
                        if (node1 instanceof TextNode) {
                            if (((TextNode) node1).text().length() > 2) {
                                res.add(((TextNode) node1).text().trim());
                            }
                        }

                        //mecz odbyty
                        if (node1 instanceof Element) {
                            List<Node> childNodes2 = jsoupUtils.getChildNodes(node1);
                            for (Node value : childNodes2) {
                                if (value instanceof TextNode) {
                                    if (((TextNode) value).text().length() > 2) {
                                        res.add(((TextNode) value).text().trim());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return res;
    }


    private boolean matchContainsTeam(Node node, String clubName) {
        List<String> teamNameFromMatch = getTeamNameFromMatch(node);
        return teamNameFromMatch.stream().anyMatch(e -> e.equals(clubName));
    }

    private List<Match> parseSeason(Element season, String clubName) {
        if (Objects.isNull(season)) {
            return new ArrayList<>();
        }
        List<Match> matches = new ArrayList<>();
        //todo POPRAWIC INDEKSY
        //get matches from season
        List<Node> events = season.childNodes();
        //loop through matches
        for (Node match : events) {
            if (match instanceof TextNode || match.childNodes().size() != 4) {
                continue;
            }
            if (matchContainsTeam(match, clubName)) {
                System.out.println("******** TRUE ************");
                System.out.println(match);
                List<Node> info = match.childNodes();
                Match matchInfo = parseMatch(info.get(2));
                matchInfo.setDesc(jsoupUtils.getTextFromNode(info.get(0)));
                matches.add(matchInfo);
            }
        }
        return matches;
    }
}
