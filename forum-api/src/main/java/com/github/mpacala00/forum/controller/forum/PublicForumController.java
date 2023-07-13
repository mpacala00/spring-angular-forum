package com.github.mpacala00.forum.controller.forum;

import com.github.mpacala00.forum.exception.model.ResourceNotFoundException;
import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.dto.category.CategoryDTO;
import com.github.mpacala00.forum.model.dto.category.CategoryPostsDTO;
import com.github.mpacala00.forum.model.dto.comment.CommentDTO;
import com.github.mpacala00.forum.model.dto.post.PostDTO;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.security.UserAuthenticationService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("public")
public class PublicForumController {

    PostServiceImpl postServiceImpl;
    CategoryServiceImpl categoryServiceImpl;
    CommentServiceImpl commentServiceImpl;

    CommentRepository commentRepository;

    PostDTOMappingService postDTOMappingService;
    CategoryDTOMappingService categoryDTOMappingService;
    CommentDTOMappingService commentDTOMappingService;

    UserAuthenticationService userAuthenticationService;

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
    public ResponseEntity<CategoryPostsDTO> getCategoryById(@PathVariable Long categoryId,
                                                            Authentication authentication) throws ResourceNotFoundException {
        Category cat = categoryServiceImpl.findById(categoryId);
        if(cat == null) {
            throw new ResourceNotFoundException(String.format("Category of id=%d not found", categoryId));
        }

        CategoryPostsDTO dto = categoryDTOMappingService
                .convertToCategoryPostsDTO(cat);

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
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId,
                                                              @RequestHeader Map<String, String> headers) {
        Optional<User> userOpt = userAuthenticationService.retrieveByRequestHeadersToken(headers);

        List<CommentDTO> comments = commentServiceImpl.getAllCommentsFromPost(postId, userOpt.map(User::getId).orElse(null));

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
