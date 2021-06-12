package com.github.mpacala00.forum.security.model;

import org.apache.commons.lang3.ArrayUtils;

public class Authority {
    //subforum discussion/post comment
    public static final String[] USER_AUTHORITIES = {
            //for viewing content exclusive to logged-in users, like other users' profiles
            "content:view"
    };

    public static final String[] MODERATOR_AUTHORITIES = ArrayUtils.addAll(USER_AUTHORITIES,
            "user:block",
            "content:delete",
            //for adding content regular users aren't allowed to
            //in case of this app creating new categories
            "content:add"
    );

    public static final String[] ADMIN_AUTHORITIES = ArrayUtils.addAll(MODERATOR_AUTHORITIES,
            "user:delete",
            "user:update"
    );
}
