package com.janwisniewski.domain.elems;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Player {
    private String firstName;
    private String lastName;
    private Team team;
}
