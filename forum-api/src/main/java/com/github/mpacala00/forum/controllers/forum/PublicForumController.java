package com.github.mpacala00.forum.controllers.forum;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.service.CategoryService;
import com.github.mpacala00.forum.service.PostService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Post> getPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("categories")
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }
}
