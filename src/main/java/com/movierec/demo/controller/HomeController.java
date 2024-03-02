package com.movierec.demo.controller;

import com.movierec.demo.dto.BoxOfficeDto;
import com.movierec.demo.dto.BoxOfficeResultDto;
import com.movierec.demo.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @Value("${tmdb.key}")
    private String tmdbKey;

    @RequestMapping("/")
    public String home(Model model) {
        String url = "https://api.themoviedb.org/3/movie/now_playing?language=ko-KR&page=1&api_key=" + tmdbKey;
        String img_url = "https://image.tmdb.org/t/p/w154";

        List<BoxOfficeResultDto> boxMovies = homeService.findMovies(url).getResults();
        for(BoxOfficeResultDto boxMovie : boxMovies){
            String img_path = boxMovie.getPoster_path();
            boxMovie.setPoster_path(img_url + img_path);
        }
        model.addAttribute("boxMovies", boxMovies);

        return "main";
    }

}
