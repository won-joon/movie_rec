package com.movierec.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class TrendDto {

    private List<String> period;

    private List<Integer> ratio;

}
