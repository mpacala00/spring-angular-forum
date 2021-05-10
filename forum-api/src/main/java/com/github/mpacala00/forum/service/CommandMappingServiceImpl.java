package com.github.mpacala00.forum.service;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.command.CategoryCommand;
import com.github.mpacala00.forum.model.command.CommentCommand;
import com.github.mpacala00.forum.model.command.PostCommand;
import com.github.mpacala00.forum.model.command.UserCommand;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CommandMappingServiceImpl implements CommandMappingService {
    
    @Override
    public CommentCommand convertCommentToCommand(Comment comment) {
        CommentCommand cmd = new CommentCommand();
        cmd.setBody(comment.getBody());
        cmd.setId(comment.getId());
        cmd.setPostDate(comment.getPostDate());
        return cmd;
    }

    @Override
    public PostCommand convertPostToCommand(Post post) {
        PostCommand cmd = new PostCommand();
        cmd.setBody(post.getBody());
        cmd.setDate(post.getPostDate());
        cmd.setId(post.getId());
        cmd.setPostComments(post.getComments()
                .stream()
                .map(this::convertCommentToCommand)
                .collect(Collectors.toList()));
        cmd.setTitle(post.getTitle());
        return cmd;
    }

    @Override
    public CategoryCommand convertCategoryToCommand(Category category) {
        CategoryCommand cmd = new CategoryCommand();
        cmd.setDescription(category.getDescription());
        cmd.setId(category.getId());
        cmd.setName(category.getName());
        cmd.setCategoryPosts(category.getPosts()
                .stream()
                .map(this::convertPostToCommand)
                .collect(Collectors.toList()));
        return cmd;
    }

    @Override
    public UserCommand convertUserToCommand(User user) {
        UserCommand cmd = new UserCommand();
        cmd.setEmail(user.getEmail());
        cmd.setEnabled(user.isEnabled());
        cmd.setId(user.getId());
        cmd.setRole(user.getRole());
        cmd.setUsername(user.getUsername());
        cmd.setUserComments(user.getComments()
                .stream()
                .map(this::convertCommentToCommand)
                .collect(Collectors.toList()));
        cmd.setUserPosts(user.getPosts()
                .stream()
                .map(this::convertPostToCommand)
                .collect(Collectors.toList()));
        return cmd;
    }
}
