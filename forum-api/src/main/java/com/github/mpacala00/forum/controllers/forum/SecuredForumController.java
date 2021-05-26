package com.github.mpacala00.forum.controllers.forum;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.service.ResourceAccess;
import com.github.mpacala00.forum.service.data.CategoryServiceImpl;
import com.github.mpacala00.forum.service.data.CommentServiceImpl;
import com.github.mpacala00.forum.service.data.PostServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class SecuredForumController {
    
    PostServiceImpl postServiceImpl;
    CategoryServiceImpl categoryServiceImpl;
    CommentServiceImpl commentServiceImpl;
    ResourceAccess resourceAccess;

    @PostMapping("/category")
    public ResponseEntity<Category> publishCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryServiceImpl.save(category), HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("category/{categoryId}/post")
    public ResponseEntity<Post> publishPostToCategory(@PathVariable String categoryId,
                                                          @RequestBody Post post,
                                                          @AuthenticationPrincipal User originalPoster) {

        //another way to get user, but annotation as argument is easier to use
        //User originalPoster = userService.findByUsername(userService.getUsernameFromToken()).get();
        post.setCreator(originalPoster);
        Post savedPost = postServiceImpl.savePost(post);
        Category category = categoryServiceImpl.findById(Long.valueOf(categoryId));
        category.addPost(savedPost);
        categoryServiceImpl.save(category);
        return new ResponseEntity<Post>(savedPost, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("post/{postId}/comment")
    public ResponseEntity<Comment> publishComment(@PathVariable String postId,
                               @RequestBody Comment comment,
                               @AuthenticationPrincipal User originalPoster) {
        comment.setCreator(originalPoster);
        Comment savedComment = commentServiceImpl.save(comment);
        Post post = postServiceImpl.findById(Long.valueOf(postId));
        post.addComment(savedComment);
        postServiceImpl.savePost(post);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }
}
