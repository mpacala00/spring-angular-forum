package com.github.mpacala00.forum.controllers.user;

import com.github.mpacala00.forum.exception.model.*;
import com.github.mpacala00.forum.model.dto.category.CategoryDTO;
import com.github.mpacala00.forum.model.dto.comment.CommentDTO;
import com.github.mpacala00.forum.model.dto.post.PostDTO;
import com.github.mpacala00.forum.model.dto.UserDTO;
import com.github.mpacala00.forum.pojos.*;
import com.github.mpacala00.forum.security.model.Role;
import com.github.mpacala00.forum.service.data.UserServiceImpl;
import com.github.mpacala00.forum.service.dto.CategoryDTOMappingService;
import com.github.mpacala00.forum.service.dto.CommentDTOMappingService;
import com.github.mpacala00.forum.service.dto.PostDTOMappingService;
import com.github.mpacala00.forum.service.dto.UserDTOMappingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.security.UserAuthenticationService;
import com.github.mpacala00.forum.service.mail.MailServiceImpl;

import java.rmi.activation.ActivateFailedException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping("/public/user")
public class PublicUserController {

    UserServiceImpl userService;
    MailServiceImpl mailServiceImpl;
    UserAuthenticationService authenticationService;

    PostDTOMappingService postDTOMappingService;
    CommentDTOMappingService commentDTOMappingService;
    CategoryDTOMappingService categoryDTOMappingService;
    UserDTOMappingService userDTOMappingService;

    @Value("${spring.mail.activation-link}")
    @NonFinal
    String activationLink;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistration userRegistration)
            throws UserNotFoundException, ActivationEmailException, UserAlreadyExistException, InvalidCredentialsException, EmailTakenException {

        if(userRegistration.getUsername().contains(" ")) {
            throw new InvalidCredentialsException("Username cannot contain spaces");
        }
        //check if confirm password is the same as password in registration form
        if(!userRegistration.getPassword().equals(userRegistration.getPasswordConfirmation())) {
            throw new InvalidCredentialsException("Passwords do not match");
        }
        if(userService.findByUsername(userRegistration.getUsername()) != null)
            throw new UserAlreadyExistException(String.format("Username %s already taken", userRegistration.getUsername()));
        if(userService.findByEmail(userRegistration.getEmail()) != null)
            throw new EmailTakenException(String.format("Email %s is already taken", userRegistration.getEmail()));
        
        User u = new User(userRegistration.getUsername(),
                userRegistration.getPassword(), userRegistration.getEmail());
        
        //enable account for now to skip email activation
        u.setEnabled(true);
        u.setRole(Role.ROLE_USER);

        User savedUser = userService.save(u);

        String token = authenticationService.login(savedUser.getUsername(), savedUser.getPassword())
                .orElseThrow(RuntimeException::new);

        //sending activation email
//        mailService.sendMail(new NotificationEmail("ForumApp - active your account",
//                savedUser.getEmail(), "Thank you for signing up, " +
//                "please click the link below to activate your account:\n"+
//                activationLink+token));

        return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);
    }

    @GetMapping("/activate-account")
    public ResponseEntity<HttpResponse> activateAccount(@RequestParam final String token) throws ActivateFailedException {
        this.authenticationService.activateAccount(token);
        boolean activated = this.authenticationService.activateAccount(token);
        if(activated) {
            return HttpResponse.createResponseEntity(HttpStatus.OK, "Successfully activated account");
        }
        throw new ActivateFailedException("An error occurred during e-mail activation");
    }
    
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserLogin login)
            throws UserNotFoundException, InvalidCredentialsException {
        String token = authenticationService.login(login.getUsername(), login.getPassword())
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));
        return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);
    }

    @GetMapping("{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) throws UserNotFoundException {
        if(userService.findByUsername(username) != null) {

            UserDTO user = userDTOMappingService.convertToDTO(userService.findByUsername(username));
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        //this exception will never be thrown as findById() will throw NullPointer first
        throw new UserNotFoundException(String.format("User %s not present", username));
    }

    @GetMapping("{username}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable String username) throws UserNotFoundException {
        if(userService.findByUsername(username) != null) {

            List<PostDTO> posts = userService.findByUsername(username).getPosts()
                    .stream()
                    .map(postDTOMappingService::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }

        //this exception will never be thrown as findById() will throw NullPointer first
        throw new UserNotFoundException(String.format("Cannot get posts from user. User %s not present", username));
    }

    @GetMapping("{username}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByUser(@PathVariable String username) throws UserNotFoundException {
        if(userService.findByUsername(username) != null) {

            List<CommentDTO> comments = userService.findByUsername(username).getComments()
                    .stream()
                    .map(commentDTOMappingService::convertToCommentPostDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }

        throw new UserNotFoundException(String.format("Cannot get comments from user. User %s not present", username));
    }

    @GetMapping("{username}/followed-categories")
    public ResponseEntity<List<CategoryDTO>> getFollowedCategoriesByUser(@PathVariable String username) throws UserNotFoundException {
        if(userService.findByUsername(username) != null) {

            List<CategoryDTO> categories = userService.findByUsername(username).getFollowedCategories()
                    .stream()
                    .map(categoryDTOMappingService::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }

        throw new UserNotFoundException(String.format("Cannot get categories from user. User %s not present", username));
    }
}
