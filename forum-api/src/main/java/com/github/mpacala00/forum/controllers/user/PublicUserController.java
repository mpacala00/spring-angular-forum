package com.github.mpacala00.forum.controllers.user;

import com.github.mpacala00.forum.model.dto.CategoryDTO;
import com.github.mpacala00.forum.model.dto.CommentDTO;
import com.github.mpacala00.forum.model.dto.PostDTO;
import com.github.mpacala00.forum.pojos.*;
import com.github.mpacala00.forum.security.model.Role;
import com.github.mpacala00.forum.service.dto.CategoryDTOMappingService;
import com.github.mpacala00.forum.service.dto.CommentDTOMappingService;
import com.github.mpacala00.forum.service.dto.PostDTOMappingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.mpacala00.forum.exception.ActivationEmailException;
import com.github.mpacala00.forum.exception.UserNotFoundException;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.security.UserAuthenticationService;
import com.github.mpacala00.forum.service.MailService;
import com.github.mpacala00.forum.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping("/public/user")
public class PublicUserController {

    UserService userService;
    MailService mailService;
    UserAuthenticationService authenticationService;

    PostDTOMappingService postDTOMappingService;
    CommentDTOMappingService commentDTOMappingService;
    CategoryDTOMappingService categoryDTOMappingService;

    @Value("${spring.mail.activation-link}")
    @NonFinal
    String activationLink;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistration userRegistration) throws UserNotFoundException, ActivationEmailException {

        //check if confirm password is the same as password in registration form
        if(!userRegistration.getPassword().equals(userRegistration.getPasswordConfirmation())) {
            return HttpResponse.createResponseEntity(HttpStatus.CONFLICT, "Passwords do not match");
        }
        else if(userService.findByUsername(userRegistration.getUsername()).isPresent())
            return HttpResponse.createResponseEntity(HttpStatus.CONFLICT, "User already exists");
        
        User u = new User(userRegistration.getUsername(),
                userRegistration.getPassword(), userRegistration.getEmail());
        
        //enable account for now to skip email activation
        u.setEnabled(true);
        u.setRole(Role.ROLE_USER);

        User savedUser = userService.save(u);

        String token = authenticationService.login(savedUser.getUsername(), savedUser.getPassword())
                .orElseThrow(() -> new RuntimeException("invalid login or password"));

        //sending activation email
//        mailService.sendMail(new NotificationEmail("ForumApp - active your account",
//                savedUser.getEmail(), "Thank you for signing up, " +
//                "please click the link below to activate your account:\n"+
//                activationLink+token));

        return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);
    }

    @GetMapping("/activate-account")
    public ResponseEntity<HttpResponse> activateAccount(@RequestParam final String token) {
        this.authenticationService.activateAccount(token);
        boolean activated = this.authenticationService.activateAccount(token);
        if(activated) {
            return HttpResponse.createResponseEntity(HttpStatus.OK, "Successfully activated account");
        }
        return HttpResponse.createResponseEntity(HttpStatus.BAD_REQUEST, "Error occurred while activating account");
    }
    
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserLogin login) throws UserNotFoundException {
        String token = authenticationService.login(login.getUsername(), login.getPassword())
                .orElseThrow(() -> new RuntimeException("invalid login or password"));
        return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);
    }

    @GetMapping("{username}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable String username) throws UserNotFoundException {
        if(userService.findByUsername(username).isPresent()) {

            List<PostDTO> posts = userService.findByUsername(username).get().getPosts()
                    .stream()
                    .map(postDTOMappingService::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }

        //this exception will never be thrown as findById() will throw NullPointer first
        throw new UserNotFoundException(String.format("Cannot get posts from user\nUser %s not present", username));
    }

    @GetMapping("{username}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByUser(@PathVariable String username) throws UserNotFoundException {
        if(userService.findByUsername(username).isPresent()) {

            List<CommentDTO> comments = userService.findByUsername(username).get().getComments()
                    .stream()
                    .map(commentDTOMappingService::convertToCommentPostDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }

        //this exception will never be thrown as findById() will throw NullPointer first
        throw new UserNotFoundException(String.format("Cannot get posts from user\nUser %s not present", username));
    }

    @GetMapping("{username}/followed-categories")
    public ResponseEntity<List<CategoryDTO>> getFollowedCategoriesByUser(@PathVariable String username) throws UserNotFoundException {
        if(userService.findByUsername(username).isPresent()) {

            List<CategoryDTO> categories = userService.findByUsername(username).get().getFollowedCategories()
                    .stream()
                    .map(categoryDTOMappingService::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }

        //this exception will never be thrown as findById() will throw NullPointer first
        throw new UserNotFoundException(String.format("Cannot get posts from user\nUser %s not present", username));
    }
}
