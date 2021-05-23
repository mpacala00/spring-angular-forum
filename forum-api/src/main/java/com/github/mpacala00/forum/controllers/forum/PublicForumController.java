package com.github.mpacala00.forum.controllers.forum;

import com.github.mpacala00.forum.model.dto.CategoryDTO;
import com.github.mpacala00.forum.model.dto.CategoryPostsDTO;
import com.github.mpacala00.forum.model.dto.CommentDTO;
import com.github.mpacala00.forum.model.dto.PostDTO;
import com.github.mpacala00.forum.service.data.CategoryServiceImpl;
import com.github.mpacala00.forum.service.data.CommentServiceImpl;
import com.github.mpacala00.forum.service.data.PostServiceImpl;
import com.github.mpacala00.forum.service.dto.CategoryDTOMappingService;
import com.github.mpacala00.forum.service.dto.CommentDTOMappingService;
import com.github.mpacala00.forum.service.dto.PostDTOMappingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("public")
public class PublicForumController {

    PostServiceImpl postServiceImpl;
    CategoryServiceImpl categoryServiceImpl;
    CommentServiceImpl commentServiceImpl;

    PostDTOMappingService postDTOMappingService;
    CategoryDTOMappingService categoryDTOMappingService;
    CommentDTOMappingService commentDTOMappingService;

    //this should be removed as they belong to a category
    @GetMapping("posts")
    public ResponseEntity<List<PostDTO>> getPosts() {
        List<PostDTO> posts = postServiceImpl.getAllPosts()
                .stream()
                .map(postDTOMappingService::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("categories")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categories = categoryServiceImpl.getAllCategories()
                .stream()
                .map(categoryDTOMappingService::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    //CategoryPostsDTO so it also contains all of its posts
    @GetMapping("category/{categoryId}")
    public ResponseEntity<CategoryPostsDTO> getCategoryById(@PathVariable Long categoryId) {
        CategoryPostsDTO dto = categoryDTOMappingService
                .convertToCategoryPostsDTO(categoryServiceImpl.findById(categoryId));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("category/{categoryId}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable Long categoryId) {
        List<PostDTO> posts = categoryServiceImpl.findById(categoryId).getPosts()
                .stream()
                .map(postDTOMappingService::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("post/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO post = postDTOMappingService.convertToDTO(postServiceImpl.findById(postId));
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("post/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) throws Exception {
        List<CommentDTO> comments = commentServiceImpl.getAllCommentsFromPost(postId)
                .stream()
                .map(commentDTOMappingService::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
