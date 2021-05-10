package com.github.mpacala00.forum.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDTO {

    Long id;
    String creator;
    String title;
    String body;
    String postDate;
}
