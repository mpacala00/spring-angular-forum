package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.User;

public interface UserLikedCommentService {

    /**
     * Designated user likes or dislikes designated comment by inserting UserLikedComment object.
     * If UserLikedComment already exist for designated comment, but the new isLike value is negation of
     * existing value, update isLike to the new value.
     * Else remove existing UserLikedComment.
     *
     * @param user      user giving reaction to a comment
     * @param commentId comment id which will receive reaction
     * @param isLike    determines whether user liked or disliked a comment
     * @return          returns information if new UserLikedComment object was created
     */
    boolean likeComment(User user, Long commentId, Boolean isLike);
}
