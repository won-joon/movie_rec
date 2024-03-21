package com.movierec.demo.dto;

import java.util.List;

public record MovieResponseDto(MovieListResult movieListResult) {
    public record MovieListResult(int totCnt, String source, List<Movie> movieList) {
        public record Movie(String movieCd, String movieNm, String movieNmEn, String prdtYear,
                            String openDt, String typeNm, String prdtStatNm, String nationAlt,
                            String genreAlt, String repNationNm, String repGenreNm,
                            List<Director> directors, List<Company> companys) {
            public record Director(String peopleNm) {}

            public record Company(String companyCd, String companyNm) {}
        }
    }
}


