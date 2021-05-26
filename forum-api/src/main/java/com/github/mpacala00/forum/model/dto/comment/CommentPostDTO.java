package com.github.mpacala00.forum.model.dto.comment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/*
 * Extended version of CommentDTO containing post ID and post title and ID of category to which post belongs
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentPostDTO extends CommentDTO{

    Long postId;
    Long postCategoryId;
    String postTitle;
}
