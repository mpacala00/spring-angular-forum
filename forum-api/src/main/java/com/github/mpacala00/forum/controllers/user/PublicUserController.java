package com.github.mpacala00.forum.controllers.user;

import com.github.mpacala00.forum.pojos.TokenResponse;
import com.github.mpacala00.forum.pojos.UserLogin;
import com.github.mpacala00.forum.security.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.github.mpacala00.forum.exception.ActivationEmailException;
import com.github.mpacala00.forum.exception.UserNotFoundException;
import com.github.mpacala00.forum.pojos.NotificationEmail;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.pojos.UserRegistration;
import com.github.mpacala00.forum.security.UserAuthenticationService;
import com.github.mpacala00.forum.service.MailService;
import com.github.mpacala00.forum.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
@RequestMapping("/public/user")
public class PublicUserController {

    private final UserService userService;
    private final MailService mailService;
    private final UserAuthenticationService authenticationService;

    private final String activationLink;

    public PublicUserController(UserService userService,
                                MailService mailService,
                                UserAuthenticationService authenticationService,
                                @Value("${spring.mail.activation-link}") String activationLink) {
        this.userService = userService;
        this.mailService = mailService;
        this.authenticationService = authenticationService;
        this.activationLink = activationLink;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRegistration userRegistration) throws UserNotFoundException, ActivationEmailException {


        //check if confirm password is the same as password in registration form
        if(!userRegistration.getPassword().equals(userRegistration.getPasswordConfirmation()))
            return "passwords do not match";
        else if(userService.findByUsername(userRegistration.getUsername()).isPresent())
            return "user already exists";
        
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

        return token;
    }

    @GetMapping("/activate-account")
    public String activateAccount(@RequestParam final String token) {
        this.authenticationService.activateAccount(token);
        boolean activated = this.authenticationService.activateAccount(token);
        if(activated) {
            return "Successfully activated account";
        }
        return "Error occurred while activating account";
    }

    //no feedback on the front-end, only exception in the back-end - change that
    @PostMapping("/login")
    TokenResponse login(@RequestBody UserLogin login) throws UserNotFoundException {
        String token = authenticationService.login(login.getUsername(), login.getPassword())
                .orElseThrow(() -> new RuntimeException("invalid login or password"));
        return new TokenResponse(token);
    }
}
