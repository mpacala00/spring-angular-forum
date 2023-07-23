package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.User;

public interface UserLikedPostService {

    boolean likeComment(User user, Long postId, Boolean isLike);
}
