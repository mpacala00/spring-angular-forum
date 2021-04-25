package com.github.mpacala00.forum.security.token;


import com.google.common.net.HttpHeaders;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.removeStart;

/*
 * The TokenAuthenticationFilter is responsible of extracting the authentication token
 * from the request headers.
 * It takes the Authorization header value and attempts to extract the token from it
 *
 * Authentication is delegated to AuthenticationManager
 * Filter is only enabled for a given set of urls
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String BEARER = "Bearer";

    public TokenAuthenticationFilter(final RequestMatcher requestAuth) {
        super(requestAuth);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        final String param = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElse(request.getParameter("t"));

        final String token = Optional.ofNullable(param)
                .map(value -> removeStart(value, BEARER))
                .map(String::trim) //remove white space from beginning
                .orElseThrow(() -> new BadCredentialsException("Missing authentication token"));

        final Authentication auth = new UsernamePasswordAuthenticationToken(token, token);

        //authenticate
        return getAuthenticationManager().authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain,
            final Authentication authResult) throws IOException, ServletException {

        super.successfulAuthentication(request, response, filterChain, authResult);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        filterChain.doFilter(request, response);
    }
}

