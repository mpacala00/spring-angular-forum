package com.github.mpacala00.forum.security.token;

import com.github.mpacala00.forum.exception.model.InvalidCredentialsException;
import com.github.mpacala00.forum.exception.model.UserNotFoundException;
import com.github.mpacala00.forum.service.data.UserServiceImpl;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.security.UserAuthenticationService;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class TokenAuthenticationService implements UserAuthenticationService {

    @NonNull TokenService tokenService;
    @NonNull UserServiceImpl userService;

    @Override
    public Optional<String> login(String username, String password)
            throws UserNotFoundException, InvalidCredentialsException {

        User u = userService.findByUsername(username);
        if(u == null) {
            throw new UserNotFoundException("User does not exist");
        }

        if(u.getPassword().equals(password)) {
            String[] authorities = userService.findByUsername(username).getRole().getAuthorities();
            ImmutableMap<String, Object> claims = ImmutableMap.of("username", username,
                    "authorities", authorities);

            return Optional.of(tokenService.expiring(claims));
        }

        throw new InvalidCredentialsException("Passwords do not match");
    }

    @Override
    public Optional<User> findByToken(String token) {
        return Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                //using flatMap() to flatten stream results to a single list
                .flatMap(userService::findOptionalByUsername);
    }

    @Override
    public boolean activateAccount(String token) {
        //find user by passed token
        Optional<User> userOptional = Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                //using flatMap() to flatten stream results to a single list
                .flatMap(userService::findOptionalByUsername);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.enableAccount(); //change enabled to true
            userService.save(user); //update the user in db
            System.out.println(user.isEnabled());//debugging
            return true;
        } //if not present do nothing
        return false;
    }

    @Override
    public void logout(User user) {
        //since tokens have expiration time there is no need to implement this
    }
}
