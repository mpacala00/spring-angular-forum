package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.dto.comment.CommentUpdateDTO;

import java.util.List;
import java.util.Set;

public interface CommentService {
    Set<Comment> getAllCommentsFromPost(Long postId);

    Set<Comment> findByUser(String user);

    Comment findById(Long id);

    Comment save(Comment comment);

    Comment update(CommentUpdateDTO commment);

    void deleteById(Long id);
}
