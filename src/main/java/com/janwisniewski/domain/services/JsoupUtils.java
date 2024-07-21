package com.janwisniewski.domain.services;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JsoupUtils {

    public List<Node> getChildNodes(Node node) {
        if (node instanceof Element) {
            Element element = (Element) node;
            return element.childNodes();
        }
        return new ArrayList<>();
    }

    public String getTextFromNode(Node node) {
        if (node instanceof Element) {
            Element element = (Element) node;
            List<Node> childNodes = element.childNodes();
            if (childNodes.size() == 1 && childNodes.get(0) instanceof TextNode) {
                return ((TextNode) childNodes.get(0)).text().trim();
            }
        }
        if (node instanceof TextNode) {
            return ((TextNode) node).text().trim();
        }
        return "";
    }

    public boolean isNodeContainsResult(Node node) {
        String text = "";
        if (!(node instanceof TextNode)) {
            throw new IllegalArgumentException("Node is not a textNode element");
        }
        text = ((TextNode) node).text().trim();
        Pattern pattern = Pattern.compile(".*\\d.*");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }


}
