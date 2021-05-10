package com.github.mpacala00.forum.controllers.forum;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.command.PostCommand;
import com.github.mpacala00.forum.model.dto.CategoryDTO;
import com.github.mpacala00.forum.model.dto.CategoryPostsDTO;
import com.github.mpacala00.forum.model.dto.CommentDTO;
import com.github.mpacala00.forum.model.dto.PostDTO;
import com.github.mpacala00.forum.service.CategoryService;
import com.github.mpacala00.forum.service.CommandMappingService;
import com.github.mpacala00.forum.service.CommentService;
import com.github.mpacala00.forum.service.PostService;
import com.github.mpacala00.forum.service.dto.CategoryDTOMappingService;
import com.github.mpacala00.forum.service.dto.CommentDTOMappingService;
import com.github.mpacala00.forum.service.dto.PostDTOMappingService;
import com.sun.mail.iap.Response;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("public")
public class PublicForumController {

    PostService postService;
    CategoryService categoryService;
    CommentService commentService;

    PostDTOMappingService postDTOMappingService;
    CategoryDTOMappingService categoryDTOMappingService;
    CommentDTOMappingService commentDTOMappingService;

    //this should be removed as they belong to a category
    @GetMapping("posts")
    public ResponseEntity<List<PostDTO>> getPosts() {
        List<PostDTO> posts = postService.getAllPosts()
                .stream()
                .map(postDTOMappingService::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("categories")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories()
                .stream()
                .map(categoryDTOMappingService::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    //CategoryPostsDTO so it also contains all of its posts
    @GetMapping("category/{categoryId}")
    public ResponseEntity<CategoryPostsDTO> getCategoryById(@PathVariable Long categoryId) {
        CategoryPostsDTO dto = categoryDTOMappingService
                .convertToCategoryPostsDTO(categoryService.findById(categoryId));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("category/{categoryId}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable Long categoryId) {
        List<PostDTO> posts = categoryService.findById(categoryId).getPosts()
                .stream()
                .map(postDTOMappingService::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("post/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO post = postDTOMappingService.convertToDTO(postService.findById(postId));
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("post/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) throws Exception {
        List<CommentDTO> comments = commentService.getAllCommentsFromPost(postId)
                .stream()
                .map(commentDTOMappingService::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
