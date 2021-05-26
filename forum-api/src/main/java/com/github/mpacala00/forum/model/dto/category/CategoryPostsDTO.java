package com.github.mpacala00.forum.model.dto.category;

import com.github.mpacala00.forum.model.dto.post.PostDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/*
 * Extended version of CategoryDTO containing posts
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryPostsDTO extends CategoryDTO{

    List<PostDTO> posts;
}
