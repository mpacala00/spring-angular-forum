package com.github.mpacala00.forum.model.dto.comment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentUpdateDTO {

    Long id;
    String body;
}
