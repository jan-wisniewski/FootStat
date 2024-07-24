package com.janwisniewski.domain.apis;

import com.janwisniewski.adapters.PlayerDto;
import com.janwisniewski.adapters.SeasonClubMatchDto;

import java.util.List;

public interface ParserApi {
    List<SeasonClubMatchDto> getInfo(String clubName);
    List<PlayerDto> getPlayers();
}
