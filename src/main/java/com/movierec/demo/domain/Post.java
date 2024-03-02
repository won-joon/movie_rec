package com.movierec.demo.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long post_id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String movietitle;

    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Post(String title, String content, String movietitle, String username) {
        this.title = title;
        this.content = content;
        this.movietitle = movietitle;
        this.username = username;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
