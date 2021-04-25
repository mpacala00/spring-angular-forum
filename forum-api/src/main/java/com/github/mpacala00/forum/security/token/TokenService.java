package com.github.mpacala00.forum.security.token;


import java.util.Map;

/**
 * Creates and validates credentials as well as Json Web Tokens
 */
public interface TokenService {

    String permanent(Map<String, Object> attributes);

    String expiring(Map<String, Object> attributes);

    /**
     * Checks if credentials are correct
     *
     * @param token
     * @return attributes if verified
     */
    Map<String, String> untrusted(String token);

    /**
     * Cheks if credentials are correct
     * @param token
     * @return attributes if verified
     */
    Map<String, String> verify(String token);
}

