package com.github.mpacala00.forum.model.command;

import com.github.mpacala00.forum.model.Post;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCommand {

    Long id;
    String name;
    String description;
    List<PostCommand> categoryPosts;
}
