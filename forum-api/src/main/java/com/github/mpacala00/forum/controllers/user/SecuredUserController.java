package com.github.mpacala00.forum.controllers.user;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.service.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class SecuredUserController {

    private final UserService userService;
    
    @GetMapping("/current")
    public User currentUser(@AuthenticationPrincipal User user) { return user; }

    @GetMapping("/{userId}/comments")
    public List<Comment> getUserComments(@PathVariable Long userId) {
        return userService.getComments(userId);
    }

    @GetMapping("/{userId}/posts")
    public List<Post> getUserPosts(@PathVariable Long userId) {
        return userService.getPosts(userId);
    }

    //todo secure with required authority
    @GetMapping("/all")
    public List<User> users() {
        return userService.getUsers();
    }
}
