package com.github.mpacala00.forum.service.command;

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
     * @param comment comment entity to be converted to dto
     * @return CommentCommand
     */
    CommentCommand convertCommentToCommand(Comment comment);

    /**
     * Converts {@link Post} to {@link PostCommand}
     * @param post post entity to be converted to dto
     * @return PostCommand
     */
    PostCommand convertPostToCommand(Post post);

    /**
     * Converts {@link Category} to {@link CategoryCommand}
     * @param category category entity to be converted to dto
     * @return CategoryCommand
     */
    CategoryCommand convertCategoryToCommand(Category category);

    /**
     * Converts {@link User} to {@link UserCommand}
     * @param user user entity to be converted to dto
     * @return UserCommand
     */
    UserCommand convertUserToCommand(User user);
}
