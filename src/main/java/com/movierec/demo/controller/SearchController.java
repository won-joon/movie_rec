package com.movierec.demo.controller;

import com.movierec.demo.dto.MovieDto;
import com.movierec.demo.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Value("${search.client.key}")
    private String clientKey;

    @GetMapping("/search")
    public String searchMovie(@RequestParam("moviename") String moviename, Model model) {
        String text = null;
        text = URLEncoder.encode(moviename, StandardCharsets.UTF_8);

        String apiUrl = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json?key=" + clientKey + "&movieNm=" + text;
        List<MovieDto> movieList = searchService.getMovieListData(apiUrl);
        model.addAttribute("movieList", movieList);

        return "search";
    }


}
