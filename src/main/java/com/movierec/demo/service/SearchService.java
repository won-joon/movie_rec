package com.movierec.demo.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movierec.demo.dto.MovieDto;
import com.movierec.demo.dto.MovieResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchService {

    private final JSONParser parser = new JSONParser();
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    @Value("${search.client.key}")
    private String clientKey;

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: " + clientRequest.method() + " " + clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    private static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response status code: " + clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie")
//            .filter(logRequest())
//            .filter(logResponse())
            .build();

    public MovieDto getMovieData(String movieName) {
        MovieResponseDto responseDto = get(movieName);
        List<MovieDto> movieList = responseDto.movieListResult().movieList().stream()
                .map(movie -> mapper.convertValue(movie, MovieDto.class))
                .collect(Collectors.toList());

        return movieList.get(0);
    }

    public List<MovieDto> getMovieListData(String movieName) {
        MovieResponseDto responseDto = get(movieName);
        System.out.println(responseDto.movieListResult().totCnt());
        List<MovieDto> movieList = responseDto.movieListResult().movieList().stream()
                .map(movie -> mapper.convertValue(movie, MovieDto.class))
                .collect(Collectors.toList());

        return movieList;
    }

    public MovieResponseDto get(String movieName) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/searchMovieList.json")
                        .queryParam("key", clientKey)
                        .queryParam("movieNm", movieName)
                        .build())
                .retrieve()
                .bodyToMono(MovieResponseDto.class)
//                .log()
                .block(); // 동기적으로 결과를 기다리고 반환
    }
}
