package com.janwisniewski.adapters;

import com.janwisniewski.domain.elems.Stats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TeamInfoDto {
    private List<SeasonClubMatchDto> seasons;
    private Stats stats;
}
