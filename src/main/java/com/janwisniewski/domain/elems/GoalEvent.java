package com.janwisniewski.domain.elems;


import com.janwisniewski.domain.elems.types.EventType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class GoalEvent extends Event {

    public GoalEvent(List<Node> elems) {
            for (Node elem : elems) {
                if (elem instanceof Element) {
                    List<Node> nodes = elem.childNodes();
                    if (!nodes.isEmpty()) {
                        TextNode min = (TextNode) elem.childNodes().get(0);
                        String parsedMinute = min.text().replaceAll("'", "").trim();
                        if (parsedMinute.contains("+")) {
                            this.min = Arrays.stream(parsedMinute.split("\\+")).map(Integer::parseInt).toList().getFirst();
                        } else {
                            this.min = Integer.parseInt(parsedMinute);
                        }
                    }
                }
                if (elem instanceof TextNode) {
                    this.surname = removeSpecificWords(((TextNode) elem).text().trim());
                }
            }
        this.eventType = EventType.GOAL;
    }

    public GoalEvent(Node e) {
        TextNode tn = (TextNode) e;
        String[] strings = extractParts(tn.text());
        if (Objects.nonNull(strings) && strings.length > 1) {
            this.surname = removeSpecificWords(strings[1]);
            this.min = Integer.parseInt(strings[0]);
            this.eventType = EventType.GOAL;
        }
    }

    private String removeSpecificWords(String input) {
        String regex = "\\(sam\\.\\)|\\(s\\)|\\(k\\)|\\s*min\\.\\s*";
        return input.replaceAll(regex, "").replaceAll("\\s{2,}", " ").trim();
    }

    public static String[] extractParts(String input) {
        String regex = "(\\d+'?)(.*)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(input.trim());

        if (matcher.matches()) {
            String before = matcher.group(1).replaceAll("'", "").trim();
            String after = matcher.group(2).trim();
            return new String[]{before, after};
        }
        return null;
    }

    @Override
    public String toString() {
        return "GoalEvent{" +
                "surname='" + surname + '\'' +
                ", min=" + min +
                ", eventType=" + eventType +
                '}';
    }
}
