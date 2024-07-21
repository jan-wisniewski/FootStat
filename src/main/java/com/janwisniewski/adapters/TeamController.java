package com.janwisniewski.adapters;

import com.janwisniewski.domain.apis.ParserApi;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class TeamController {

    private final ParserApi parserApi;

    @GetMapping("/team")
    List<SeasonClubMatchDto> getInfo() {
        return parserApi.getInfo("Zagłębie Lubin");
    }

}
