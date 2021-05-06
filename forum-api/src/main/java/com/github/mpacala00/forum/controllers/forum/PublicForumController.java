package com.github.mpacala00.forum.controllers.forum;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.command.PostCommand;
import com.github.mpacala00.forum.service.CategoryService;
import com.github.mpacala00.forum.service.CommandMappingService;
import com.github.mpacala00.forum.service.PostService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("public")
@CrossOrigin(origins = "*")
public class PublicForumController {

    PostService postService;
    CategoryService categoryService;

    //this should be removed as they belong to a category
    @GetMapping("posts")
    public ResponseEntity<List<Post>> getPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @GetMapping("categories")
    public ResponseEntity<List<Category>> getCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @GetMapping("category/{categoryId}/posts")
    public ResponseEntity<List<Post>> getPostsByCategory(@PathVariable Long categoryId) {
        List<Post> posts = categoryService.findById(categoryId).getPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("post/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        return new ResponseEntity<>(postService.findById(postId), HttpStatus.OK);
    }
}
