package com.github.mpacala00.forum.model.dto.category;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDTO {

    Long id;
    String name;
    String description;
    boolean isUserFollowing;
}
