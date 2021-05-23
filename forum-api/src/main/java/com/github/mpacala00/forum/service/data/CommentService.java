package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Comment;

import java.util.List;
import java.util.Set;

public interface CommentService {
    Set<Comment> getAllCommentsFromPost(Long postId);

    Set<Comment> findByUser(String user);

    Comment save(Comment comment);
}
