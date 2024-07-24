package com.janwisniewski.domain.services;

import com.janwisniewski.adapters.PlayerDto;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MapperService {

    private final ResourceLoader resourceLoader;
    private final List<PlayerDto> playersMap;

    public List<PlayerDto> findPlayer(String player) {
        List<PlayerDto> players = playersMap.stream().filter(e -> e.getName().equals(player)).toList();
        return (players.isEmpty()) ? null : players;
    }

    public List<PlayerDto> parsePlayers() {
        String fileContent;
        try  {
            Resource resource = resourceLoader.getResource("classpath:players.txt");
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            fileContent = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Pattern pattern = Pattern.compile("<option value=\"(\\d+)\">(.*?) \\(.*?\\)</option>");
        Matcher matcher = pattern.matcher(fileContent);

        while (matcher.find()) {
            String id = matcher.group(1);
            String name = matcher.group(2);
            playersMap.add(new PlayerDto(id, name));
        }

        return playersMap;
    }

}
