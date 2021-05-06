package com.github.mpacala00.forum.controllers.user;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<User> currentUser(@AuthenticationPrincipal User user) { 
        return new ResponseEntity<>(user, HttpStatus.OK); 
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<List<Comment>> getUserComments(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getComments(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getPosts(userId), HttpStatus.OK);
    }

    //todo secure with required authority
    @GetMapping("/all")
    public ResponseEntity<List<User>> users() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
}
