package com.movierec.demo.service;


import com.movierec.demo.domain.Post;
import com.movierec.demo.dto.PostDto;
import com.movierec.demo.repository.PostRepository;
import com.movierec.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Long save(PostDto postDto) {
        Post post = postRepository.save(postDto.toEntity());
        userRepository.findByUsername(postDto.getUsername()).get().addPost(post);

        return post.getPost_id();
    }

    @Transactional
    public Long update(PostDto postDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        post.update(postDto.getTitle(), postDto.getContent());

        return id;
    }

    public List<Post> findByMvtitle(String mvtitle) {
        return postRepository.findByMovietitle(mvtitle);
    }


    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        postRepository.delete(post);
    }
}
