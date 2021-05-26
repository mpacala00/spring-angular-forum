package com.github.mpacala00.forum.security;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.service.data.CommentService;
import com.github.mpacala00.forum.service.data.PostService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

//great article about @PreAuthroize:
//https://lyubomyr-shaydariv.github.io/posts/2016-08-07-spring-security-preauthorize-annotation-custom-types-and-inspectable-dsl-support/
@Component("RA")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceAccessImpl implements ResourceAccess {

    PostService postService;
    CommentService commentService;

    @Override
    public boolean checkIfPostOwner(@Nonnull final User u, @Nonnull final Long postId) {
        Post post = postService.findById(postId);
        return post.getCreator().equals(u);
    }

    @Override
    public boolean checkIfCommentOwner(@Nonnull final User u, @Nonnull final Long commentId) {
        Comment comment = commentService.findById(commentId);
        return comment.getCreator().equals(u);
    }

}
