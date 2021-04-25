package com.github.mpacala00.forum.service;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.dto.CommentDto;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.repository.PostRepository;
import com.github.mpacala00.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment postComment(CommentDto commentDto, Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            Comment comment = new Comment(commentDto.getUsername(), commentDto.getBody());
            comment.setPost(post.get());
            //not checking if username exist because it will come from decoded token
            User creator = userRepository.findByUsername(commentDto.getUsername()).get();
            Comment savedComment = commentRepository.save(comment); //save to get ID
            post.get().getCommentList().add(savedComment);
            postRepository.save(post.get());
            userRepository.save(creator);

            return savedComment;
        }
        throw new NullPointerException(String.format("Post of id=%d does not exist", postId));
    }

    public List<Comment> getAllCommentsFromPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            return post.get().getCommentList();
        }
        throw new NullPointerException();
    }

    public List<Comment> findByUser(String user) {
        return commentRepository.findByCreator(user);
    }
}
