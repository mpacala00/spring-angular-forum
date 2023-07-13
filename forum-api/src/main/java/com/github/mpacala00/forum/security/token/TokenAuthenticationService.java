package com.github.mpacala00.forum.security.token;

import com.github.mpacala00.forum.exception.model.InvalidCredentialsException;
import com.github.mpacala00.forum.exception.model.UserLockedException;
import com.github.mpacala00.forum.exception.model.UserNotFoundException;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.security.UserAuthenticationService;
import com.github.mpacala00.forum.service.data.UserServiceImpl;
import com.github.mpacala00.forum.util.TokenUtil;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class TokenAuthenticationService implements UserAuthenticationService {

    TokenService tokenService;
    UserServiceImpl userService;

    @Override
    public Optional<String> login(String username, String password)
            throws UserNotFoundException, InvalidCredentialsException, UserLockedException {

        User u = userService.findByUsername(username);
        if(u == null) {
            throw new UserNotFoundException("User does not exist");
        }
        
        if(!u.isNotLocked()) {
            throw new UserLockedException("Account has been locked");
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
                .flatMap(userService::findOptionalByUsername);
    }

    @Override
    public boolean activateAccount(String token) {
        Optional<User> userOptional = Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                .flatMap(userService::findOptionalByUsername);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.enableAccount();
            userService.save(user);
            System.out.println(user.isEnabled());
            return true;
        }
        return false;
    }

    @Override
    public Optional<User> retrieveByRequestHeadersToken(Map<String, String> headers) {
        String token = headers.get(HttpHeaders.AUTHORIZATION.toLowerCase());

        if (StringUtils.isEmpty(token)) {
            return Optional.empty();
        }

        if (token.startsWith("Bearer")) {
            token = TokenUtil.removeBearer(token);
        }

        return findByToken(token);
    }

    @Override
    public void logout(User user) {
        //since tokens have expiration time there is no need to implement this
    }
}
