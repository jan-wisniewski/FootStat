package com.janwisniewski.domain.elems;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Season {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    public Season(String text) {
        String convertedDate = text.replaceAll("[^0-9.]", "");
        String substring = convertedDate.substring(convertedDate.length() - 4);
        long l = Long.parseLong(substring) - 1;
        this.name = String.valueOf(l) + "/" + substring;
    }
}

