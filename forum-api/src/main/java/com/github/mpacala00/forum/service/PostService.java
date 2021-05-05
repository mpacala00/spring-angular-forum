package com.github.mpacala00.forum.service;

import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {

    PostRepository postRepository;
    CommentRepository commentRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post savePost(Post post) {
        if(post.getDate() == null) {
            post.setDate(LocalDateTime.now());
        }
        return postRepository.save(post);
    }

    public List<Post> findByUser(String user) {
        return postRepository.findByCreator(user);
    }

    public Post findById(Long id) {
        //todo check if not null
        return postRepository.findById(id).get();
    }

}
