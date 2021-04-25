package com.github.mpacala00.forum.controllers.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.service.UserService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class SecuredUserController {

    private final UserService userService;

    @GetMapping("/current")
    public User currentUser(@AuthenticationPrincipal User user) { return user; }

    @GetMapping("/find-all")
    public List<User> users() {
        return userService.getUsers();
    }
}
