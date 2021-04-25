package com.github.mpacala00.forum.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserRegistration {

    private String username;
    private String password;
    private String passwordConfirmation;
    private String email;
}
