package com.movierec.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class MovieDto {

    private String movieCd;

    private String movieNm;

    private String openDt;

    private String genreAlt;

    private List<String> directorList;
}
