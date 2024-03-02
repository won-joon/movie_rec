package com.movierec.demo.controller;


import com.movierec.demo.domain.Post;
import com.movierec.demo.dto.PostDto;
import com.movierec.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final PostService postService;

    @PostMapping("/review/{movieNm}")
    public ResponseEntity<PostDto> save(@PathVariable("movieNm") String movieNm, PostDto postDto) {
        postDto.setMovieTitle(movieNm);
        postService.save(postDto);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @PutMapping("/review/{movieNm}/{id}")
    public ResponseEntity<Long> update(PostDto postDto, @PathVariable("id") Long id) {
        postService.update(postDto, id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @DeleteMapping("/review/{movieNm}/{id}")
    public String delete(@PathVariable("movieNm") String movieNm, @PathVariable("id") Long id, Model model)  {
        postService.delete(id);
        model.addAttribute("posts", postService.findByMvtitle(movieNm));

        return "/moviedetail :: #review-card";
    }


}
