package com.github.mpacala00.forum.security.token;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.github.mpacala00.forum.security.UserAuthenticationService;

import java.util.Optional;

/**
 * responsible for finding the user by authentication token
 */
@Component
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @NonNull
    final UserAuthenticationService auth;

    @Override
    protected void additionalAuthenticationChecks(UserDetails details,
                                                  UsernamePasswordAuthenticationToken auth)
            throws AuthenticationException {
        //nothing necessary to do here
    }

    @Override
    protected UserDetails retrieveUser(final String username,
                                       UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        final Object token = authentication.getCredentials();

        return Optional
                .ofNullable(token)
                .map(String::valueOf)
                .flatMap(auth::findByToken)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with authentication token: "+token));
    }
}

