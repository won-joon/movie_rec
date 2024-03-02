package com.movierec.demo.dto;

import com.movierec.demo.domain.Post;
import com.movierec.demo.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class PostDto {
    private String title;
    private String content;
    private String movieTitle;
    private String username;
    private String status;


    public Post toEntity() {
        return Post.builder().title(title).content(content).movietitle(movieTitle).username(username).build();
    }
}
