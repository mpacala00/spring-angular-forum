package com.github.mpacala00.forum.security.token;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.security.UserAuthenticationService;
import com.github.mpacala00.forum.service.UserService;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class TokenAuthenticationService implements UserAuthenticationService {

    @NonNull TokenService tokenService;
    @NonNull UserService userService;

    /**
     *
     * @param username
     * @param password
     * @return
     * token expiration time is set in application.yml
     */
    @Override
    public Optional<String> login(String username, String password) {

        String[] authorities = userService.findByUsername(username).get().getRole().getAuthorities();
        ImmutableMap<String, Object> claims = ImmutableMap.of("username", username,
                "authorities", authorities);

        return userService
                .findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                //return a user that has a token with the passed username att
                .map(user -> tokenService.expiring(claims));
    }

    @Override
    public Optional<User> findByToken(String token) {
        return Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                //using flatMap() to flatten stream results to a single list
                .flatMap(userService::findByUsername);
    }

    @Override
    public boolean activateAccount(String token) {
        //find user by passed token
        Optional<User> userOptional = Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                //using flatMap() to flatten stream results to a single list
                .flatMap(userService::findByUsername);
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
