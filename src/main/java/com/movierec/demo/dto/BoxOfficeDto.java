package com.movierec.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class BoxOfficeDto {

    private String page;
    private List<BoxOfficeResultDto> results;
    private String total_pages;
    private String total_results;
}
