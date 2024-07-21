package com.janwisniewski.domain.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

@Service
public class WebCrawlerService {

    public String getHtmlContent(String link) {
        if (Objects.isNull(link)) {
            return "";
        }
        try {
            URL url = URI.create(link).toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Elements getHtmlElements(String content, String selector) {
        Document doc = Jsoup.parse(content);
        return doc.select(selector);
    }


}
