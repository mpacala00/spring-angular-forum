package com.github.mpacala00.forum.service;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.command.CategoryCommand;
import com.github.mpacala00.forum.model.command.CommentCommand;
import com.github.mpacala00.forum.model.command.PostCommand;
import com.github.mpacala00.forum.model.command.UserCommand;

public interface CommandMappingService {

    /**
     * Converts {@link Comment} to {@link CommentCommand}
     * @param comment
     * @return CommentCommand
     */
    CommentCommand convertCommentToCommand(Comment comment);

    /**
     * Converts {@link Post} to {@link PostCommand}
     * @param post
     * @return PostCommand
     */
    PostCommand convertPostToCommand(Post post);

    /**
     * Converts {@link Category} to {@link CategoryCommand}
     * @param category
     * @return CategoryCommand
     */
    CategoryCommand convertCategoryToCommand(Category category);

    /**
     * Converts {@link User} to {@link UserCommand}
     * @param user
     * @return UserCommand
     */
    UserCommand convertUserToCommand(User user);
}
