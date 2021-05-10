package com.github.mpacala00.forum.model.dto;

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
