package com.movierec.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class BoxOfficeResultDto {
    private Long id;
    private String original_language;
    private String overview;
    private Long popularity;
    private String poster_path;
    private String release_date;
    private String title;
    private Long vote_average;
    private Long vote_count;
}
