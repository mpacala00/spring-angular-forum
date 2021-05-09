package com.github.mpacala00.forum.service;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.repository.PostRepository;
import com.github.mpacala00.forum.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Comment> getAllCommentsFromPost(Long postId) throws Exception {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            return new ArrayList<>(post.get().getComments());
        }
        throw new NullPointerException();
    }
    
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> findByUser(String user) {
        return commentRepository.findByCreator(user);
    }
}
