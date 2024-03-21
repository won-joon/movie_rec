package com.movierec.demo.controller;

import com.movierec.demo.auth.PrincipalDetails;
import com.movierec.demo.domain.Post;
import com.movierec.demo.dto.MovieDto;
import com.movierec.demo.dto.PostDto;
import com.movierec.demo.dto.TrendDto;
import com.movierec.demo.service.NaverApiService;
import com.movierec.demo.service.PostService;
import com.movierec.demo.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final SearchService searchService;
    private final NaverApiService naverApiService;
    private final PostService postService;

    @Value("${search.client.key}")
    private String clientKey;


    /*
    영화 제목을 통해 영화정보와 검색어 트렌드를 가져옴
     */
    @GetMapping("/movie/detail/{movieNm}")
    public String movieDetails(@PathVariable("movieNm") String movieNm, Model model){
        MovieDto movieData = searchService.getMovieData(movieNm);
        model.addAttribute("movieData", movieData);

        String naverApiUrl = "https://openapi.naver.com/v1/datalab/search";
        TrendDto trendList = naverApiService.getTrendData(naverApiUrl, movieNm);
        model.addAttribute("trendList", trendList);

        model.addAttribute("posts", postService.findByMvtitle(movieNm));


        return "moviedetail";
    }

    @GetMapping("/movie/detail/{movieNm}/save")
    public String movieReview(@PathVariable("movieNm") String movieNm, @AuthenticationPrincipal PrincipalDetails userDetails, Model model) {
        PostDto postDto = new PostDto();
        postDto.setMovieTitle(movieNm);
        postDto.setUsername(userDetails.getUsername());
        postDto.setStatus("save");

        model.addAttribute("post", postDto);
//        model.addAttribute("movieNm", movieNm);
//        model.addAttribute("userNm", userDetails.getUsername());
//        model.addAttribute("status", "save");

        return "review";
    }

    @PostMapping("/movie/detail/{movieNm}/update")
    public String reviewUpdate(@PathVariable("movieNm") String movieNm, @RequestParam("post_id") String id, PostDto postDto, Model model) {
        postDto.setMovieTitle(movieNm);
        postDto.setStatus("update");
        model.addAttribute("post", postDto);
//        model.addAttribute("userNm", postDto.getUsername());
//        model.addAttribute("title", postDto.getTitle());
//        model.addAttribute("content", postDto.getContent());
//        model.addAttribute("status", "update");
        model.addAttribute("id", id);

        return "review";
    }


}
