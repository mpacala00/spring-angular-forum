package com.github.mpacala00.forum.security;

import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * since this will be a rest api, we should return 401 unauthorized resonse
 * instead of redirecting
 */
public class NoRedirectStrategy implements RedirectStrategy {

    @Override
    public void sendRedirect(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final String url) throws IOException {
        //dont redirect in pure rest
    }
}
