package com.github.mpacala00.forum.model.command;

import com.github.mpacala00.forum.security.model.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCommand {
    
    Long id;
    String username;
    String email;
    boolean enabled;
    Role role;
    List<PostCommand> userPosts;
    List<CommentCommand> userComments;
}
