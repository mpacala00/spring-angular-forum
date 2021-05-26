package com.github.mpacala00.forum.service;

import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.security.model.Role;

import javax.annotation.Nonnull;

public interface ResourceAccess {

    boolean checkIfPostOwner(@Nonnull final User u, @Nonnull final Long postId);
    boolean checkIfCommentOwner(@Nonnull final User u, @Nonnull final Long commentId);
}
