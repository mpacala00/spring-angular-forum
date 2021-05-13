package com.github.mpacala00.forum.controllers.user;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.dto.UserDTO;
import com.github.mpacala00.forum.service.dto.UserDTOMappingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
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

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/user")
public class SecuredUserController {

    UserService userService;
    UserDTOMappingService mappingService;

    @Transactional
    @GetMapping("/current")
    public ResponseEntity<UserDTO> currentUser(@AuthenticationPrincipal User user) {
        UserDTO dto = mappingService.convertToDTO(user);
        return new ResponseEntity<>(dto, HttpStatus.OK);
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
