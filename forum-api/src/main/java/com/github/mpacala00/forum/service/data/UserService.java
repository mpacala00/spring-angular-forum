package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.exception.model.UserNotFoundException;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends CrudService<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
    List<Comment> getComments(Long userId);
    List<Post> getPosts(Long userId);

    /**
     * Finds user by username and returns it as optional
     * @param username
     * @return Optional of user
     */
    Optional<User> findOptionalByUsername(String username);
    
    void blockUnblockUser(Long userId) throws UserNotFoundException;
}
