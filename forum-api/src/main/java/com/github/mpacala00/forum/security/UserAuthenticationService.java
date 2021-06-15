package com.github.mpacala00.forum.security;


import com.github.mpacala00.forum.exception.model.InvalidCredentialsException;
import com.github.mpacala00.forum.exception.model.UserLockedException;
import com.github.mpacala00.forum.exception.model.UserNotFoundException;
import com.github.mpacala00.forum.model.User;

import java.util.Optional;

/**
 * Used for logging users in and out + providing tokens
 */
public interface UserAuthenticationService {

    /**
     * Logs in with the given {@code username} and {@code password}.
     * token expiration time is set in application.yml
     *
     * @param username
     * @param password
     * @return an {@link Optional} of a user when login succeeds
     */
    Optional<String> login(String username, String password) throws UserNotFoundException, InvalidCredentialsException, UserLockedException;

    /**
     * Finds a user by its dao-key.
     *
     * @param token user dao key
     * @return
     */
    Optional<User> findByToken(String token);

    /**
     *
     * @param token
     * @return boolean if account was actually activated
     */
    boolean activateAccount(String token);

    /**
     * Logs out the given input {@code user}.
     *
     * @param user the user to logout
     */
    void logout(User user);
}

