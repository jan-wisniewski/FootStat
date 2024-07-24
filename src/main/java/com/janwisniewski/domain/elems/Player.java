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
public class Player {
    private String firstName;
    private String lastName;
    private List<String> id;
}
