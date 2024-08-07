package com.janwisniewski.domain.apis;

import com.janwisniewski.adapters.PlayerDto;
import com.janwisniewski.adapters.SeasonClubMatchDto;
import com.janwisniewski.adapters.TeamInfoDto;

import java.util.List;

public interface ParserApi {
    TeamInfoDto getInfo(String clubName);
    List<PlayerDto> getPlayers();
}
