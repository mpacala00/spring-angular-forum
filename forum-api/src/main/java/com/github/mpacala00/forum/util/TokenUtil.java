package com.github.mpacala00.forum.util;

import org.apache.commons.lang3.StringUtils;

public final class TokenUtil {

    public static final String BEARER = "Bearer";
    public static String removeBearer(String token) {
        return StringUtils.removeStart(token, BEARER).trim();
    }
}
