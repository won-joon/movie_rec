package com.movierec.demo.service;

import com.movierec.demo.dto.BoxOfficeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class HomeService {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultCookie("cookieKey", "cookieValue")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
            .build();



    public BoxOfficeDto findMovies(String BASE_URL) {
        BoxOfficeDto boxMovies = webClient.get()
                .uri(BASE_URL)
                .retrieve()
                .bodyToMono(BoxOfficeDto.class)
                .block();

        return boxMovies;
    }
}
