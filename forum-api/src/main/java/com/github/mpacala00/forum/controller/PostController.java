package com.github.mpacala00.forum.controller;

import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.dto.post.PostDTO;
import com.github.mpacala00.forum.model.dto.post.PostUpdateDTO;
import com.github.mpacala00.forum.pojos.HttpResponse;
import com.github.mpacala00.forum.service.data.PostService;
import com.github.mpacala00.forum.service.dto.PostDTOMappingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("categories/{categoryId}/posts")
public class PostController {

    PostService postService;
    PostDTOMappingService postDTOMappingService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable Long categoryId) {
        List<PostDTO> posts = postService.findByCategoryIdMapToDTO(categoryId);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO post = postDTOMappingService.convertToDTO(postService.findById(postId));

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Post> createPostInCategory(@PathVariable Long categoryId, @RequestBody Post post, //todo use DTO
            @AuthenticationPrincipal User user) {
        post.setCreator(user); //todo maybe handle this on the frontend
        Post savedPost = postService.savePostInCategory(post, categoryId);

        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    //@P annotated args are marked as parameters and then can be accessed by security
    //@RA means context is calling for resourceAccess bean
    @PutMapping("/{postId}")
    @PreAuthorize("@RA.checkIfPostOwner(#user, #post.getId())")
    public ResponseEntity<Post> updatePost(@AuthenticationPrincipal @P("user") User user,
            @PathVariable @P("postId") String postId, @RequestBody @P("post") PostUpdateDTO postUpdateDTO) {
        Post savedPost = postService.update(postUpdateDTO, Long.valueOf(postId));

        return new ResponseEntity<>(savedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("@RA.checkIfPostOwner(#user, #postId) || hasAuthority('content:delete')")
    public ResponseEntity<HttpResponse> deletePost(@AuthenticationPrincipal @P("user") User user,
                                                   @PathVariable @P("postId") String postId) {

        postService.deleteById(Long.valueOf(postId));
        return HttpResponse.createResponseEntity(HttpStatus.OK, "Post successfully deleted");
    }
}
