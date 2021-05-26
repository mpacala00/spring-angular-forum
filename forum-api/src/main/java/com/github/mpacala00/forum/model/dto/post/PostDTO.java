package com.github.mpacala00.forum.model.dto.post;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDTO {

    Long id;
    Long categoryId;
    String creator;
    String title;
    String body;
    String postDate;
}
