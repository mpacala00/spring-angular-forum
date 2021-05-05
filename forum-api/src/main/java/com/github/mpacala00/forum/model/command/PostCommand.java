package com.github.mpacala00.forum.model.command;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCommand {
    Long id;
    String title;
    String body;
    LocalDateTime date;
    List<CommentCommand> postComments;
}
