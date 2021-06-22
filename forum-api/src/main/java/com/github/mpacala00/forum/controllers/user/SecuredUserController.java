package com.github.mpacala00.forum.controllers.user;

import com.github.mpacala00.forum.exception.model.UserNotFoundException;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.dto.UserDTO;
import com.github.mpacala00.forum.pojos.HttpResponse;
import com.github.mpacala00.forum.security.model.Role;
import com.github.mpacala00.forum.service.data.UserServiceImpl;
import com.github.mpacala00.forum.service.dto.UserDTOMappingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.codehaus.jackson.node.TextNode;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/user")
public class SecuredUserController {

    UserServiceImpl userService;
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

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<Set<User>> users() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpResponse> deleteUserById(@PathVariable("userId") String userId) {
        userService.deleteById(Long.valueOf(userId));
        return HttpResponse.createResponseEntity(HttpStatus.OK, "User successfully deleted");
    }

    @GetMapping("/{userId}/block")
    public ResponseEntity<HttpResponse> blockUser(@PathVariable("userId") String userId) throws UserNotFoundException {
        userService.blockUser(Long.valueOf(userId));
        return HttpResponse.createResponseEntity(HttpStatus.OK, "User successfuly blocked");
    }

    @PreAuthorize("hasAuthority('user:update')")
    @PatchMapping(value = "/{userId}/role", consumes = "text/plain")
    public User updateUserRole(@PathVariable("userId") String userId,
                                                       @RequestBody String role) {
        User user = userService.findById(Long.valueOf(userId));
        Role roleToSet = Role.valueOf(role);
        user.setRole(roleToSet);
        userService.save(user);
        return user;
    }
}
