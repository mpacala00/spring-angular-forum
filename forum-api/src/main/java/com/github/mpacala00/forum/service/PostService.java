package com.github.mpacala00.forum.service;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.repository.CommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {

    PostRepository postRepository;
    CommentRepository commentRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void insertPost(Post post) {
        postRepository.save(post);
    }

    public List<Post> findByUser(String user) {
        return postRepository.findByCreator(user);
    }

}
