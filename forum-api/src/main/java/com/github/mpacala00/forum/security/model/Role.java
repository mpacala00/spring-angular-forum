package com.github.mpacala00.forum.security.model;

public enum Role {

    ROLE_USER(Authority.USER_AUTHORITIES),
    ROLE_MODERATOR(Authority.MODERATOR_AUTHORITIES),
    ROLE_ADMIN(Authority.ADMIN_AUTHORITIES);
    String[] authorities;

    Role(String... authorities) { //Object... <- can be either null, one object or an array of objects
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
