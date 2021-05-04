package com.github.mpacala00.forum.controllers.forum;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.service.CategoryService;
import com.github.mpacala00.forum.service.CommentService;
import com.github.mpacala00.forum.service.PostService;
import com.github.mpacala00.forum.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@CrossOrigin(origins = "*")
public class SecuredForumController {
    
    PostService postService;
    CategoryService categoryService;
    CommentService commentService;
    UserService userService;

    @PostMapping("/category")
    public Category publishCategory(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @PostMapping("category/{categoryId}/post")
    public Category publishPostToCategory(@PathVariable Long categoryId,
                                          @RequestBody Post post,
                                          @AuthenticationPrincipal User originalPoster) {

        //another way to get user, but annotation as argument is easier to use
        //User originalPoster = userService.findByUsername(userService.getUsernameFromToken()).get();
        post.setCreator(originalPoster);
        Post savedPost = postService.savePost(post);
        Category category = categoryService.findById(categoryId);
        category.addPost(savedPost);
        return categoryService.save(category);
    }

    @PostMapping("post/{postId}/comment")
    public Post publishComment(@PathVariable Long postId,
                               @RequestBody Comment comment,
                               @AuthenticationPrincipal User originalPoster) {
        comment.setCreator(originalPoster);
        Comment savedComment = commentService.save(comment);
        Post post = postService.findById(postId);
        post.addComment(savedComment);
        return postService.savePost(post);
    }
}
