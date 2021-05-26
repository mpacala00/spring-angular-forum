package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.dto.comment.CommentUpdateDTO;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.repository.PostRepository;
import com.github.mpacala00.forum.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public Set<Comment> getAllCommentsFromPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.map(value -> new HashSet<>(value.getComments())).orElse(null);
    }

    @Override
    public Set<Comment> findByUser(String user) {
        return new HashSet<>(commentRepository.findByCreator(user));
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(CommentUpdateDTO commment) {
        if(commentRepository.findById(commment.getId()).isPresent()) {
            Comment commentToUpdate = commentRepository.findById(commment.getId()).get();
            commentToUpdate.setBody(commment.getBody());
            commentToUpdate.setPostDate(LocalDateTime.now());
            return commentRepository.save(commentToUpdate);
        }
        throw new NullPointerException(String.format("Comment of id=%d does not exist", commment.getId()));
    }
}
