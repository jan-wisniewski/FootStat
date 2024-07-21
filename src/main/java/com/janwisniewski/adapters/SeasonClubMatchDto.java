package com.janwisniewski.adapters;

import com.janwisniewski.domain.elems.Match;
import com.janwisniewski.domain.elems.Season;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class SeasonClubMatchDto {
    private Season season;
    private List<Match> matchList;
}
