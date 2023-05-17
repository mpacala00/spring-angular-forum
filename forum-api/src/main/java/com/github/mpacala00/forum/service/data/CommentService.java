package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.exception.model.ResourceNotFoundException;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.dto.comment.CommentDTO;
import com.github.mpacala00.forum.model.dto.comment.CommentUpdateDTO;

import java.util.List;
import java.util.Set;

public interface CommentService {
    List<CommentDTO> getAllCommentsFromPost(Long postId, Long tokenUserId);

    CommentDTO getCommentDto(Long commentId, Long tokenUserId) throws ResourceNotFoundException;

    /**
     * Iterates over Set of UserLikedComment related to comment entity checking if any record
     * contains userId
     * @param comment comment to be checked if liked by user
     * @param userId id of user to be checked if comment was liked
     * @return null - no UserLikedComment present, true - comment liked, false - comment disliked
     */
    Boolean isLikedByUser(Comment comment, Long userId);

    Set<Comment> findByUser(String user);

    Comment findById(Long id);

    Comment save(Comment comment);

    Comment update(CommentUpdateDTO commment);

    void deleteById(Long id);
}
