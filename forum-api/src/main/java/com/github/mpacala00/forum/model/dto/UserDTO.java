package com.github.mpacala00.forum.model.dto;

import com.github.mpacala00.forum.security.model.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    Long id;
    String username;
    String email;
    Role role;
}
