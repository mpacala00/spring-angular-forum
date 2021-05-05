package com.github.mpacala00.forum.model.command;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCommand {

    Long id;
    String body;
    LocalDateTime postDate;
}
