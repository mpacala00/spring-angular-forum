package com.github.mpacala00.forum.service;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.dto.CommentDto;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.repository.PostRepository;
import com.github.mpacala00.forum.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MappingService mappingService;

    public Comment postComment(CommentDto commentDto) {
        Optional<Post> post = postRepository.findById(commentDto.getPostId());
        if(post.isPresent()) {
//            Comment comment = new Comment(commentDto.getCreator(), commentDto.getBody());
            Comment comment = mappingService.dtoToComment(commentDto);
            //not checking if username exist because it will come from decoded token
            User creator = userRepository.findByUsername(commentDto.getCreator()).get();
            Comment savedComment = commentRepository.save(comment); //save to get ID
            post.get().getComments().add(savedComment);
            postRepository.save(post.get());
            userRepository.save(creator);
            //adding comment to comments in User entity missing

            return savedComment;
        }
        throw new NullPointerException(String.format("Post of id=%d does not exist", commentDto.getPostId()));
    }

    public List<Comment> getAllCommentsFromPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
//            return post.get().getCommentList();
        }
        throw new NullPointerException();
    }

    public List<Comment> findByUser(String user) {
        return commentRepository.findByCreator(user);
    }
}
