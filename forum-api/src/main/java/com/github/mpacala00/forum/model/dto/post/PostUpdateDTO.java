package com.github.mpacala00.forum.model.dto.post;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostUpdateDTO {

    Long id; //todo get rid of this
    String title;
    String body;
}
