package com.janwisniewski.adapters;

import com.janwisniewski.domain.apis.ParserApi;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
@AllArgsConstructor
public class PlayerController {

    private ParserApi parserApi;

    @PostMapping("/parse")
    public List<PlayerDto> parsePlayerFromFile() {
        return parserApi.getPlayers();
    }

}
