package com.janwisniewski.domain.elems;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TeamSquad {
    private List<Player> first11;
    private List<Player> reserves;
}
