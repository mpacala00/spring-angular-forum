package com.github.mpacala00.forum.controller.forum;

import com.github.mpacala00.forum.exception.model.ResourceNotFoundException;
import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.dto.category.CategoryPostsDTO;
import com.github.mpacala00.forum.model.dto.comment.CommentUpdateDTO;
import com.github.mpacala00.forum.model.dto.post.PostUpdateDTO;
import com.github.mpacala00.forum.pojos.HttpResponse;
import com.github.mpacala00.forum.service.data.CategoryServiceImpl;
import com.github.mpacala00.forum.service.data.CommentServiceImpl;
import com.github.mpacala00.forum.service.data.PostServiceImpl;
import com.github.mpacala00.forum.service.data.UserService;
import com.github.mpacala00.forum.service.dto.CategoryDTOMappingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
public class SecuredForumController {
    
    PostServiceImpl postServiceImpl;
    CategoryServiceImpl categoryServiceImpl;
    CommentServiceImpl commentServiceImpl;
    CategoryDTOMappingService categoryDTOMappingService;
    UserService userService;

    //GET this when user is logged in
    //check if user is following this category and set boolean in dto accordingly
    @GetMapping("category/{categoryId}")
    public ResponseEntity<CategoryPostsDTO> getCategoryById(@PathVariable Long categoryId,
                                                            @AuthenticationPrincipal User user)
            throws ResourceNotFoundException {

        Category cat = categoryServiceImpl.findById(categoryId);
        if(cat == null) {
            throw new ResourceNotFoundException(String.format("Category of id=%d not found", categoryId));
        }

        CategoryPostsDTO dto = categoryDTOMappingService
                .convertToCategoryPostsDTO(cat);

        if(user != null) {
            if(user.getFollowedCategories().contains(cat)) {
                dto.setUserFollowing(true);
            }
        }

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/category")
    @PreAuthorize("hasAuthority('content:add')")
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
        Post savedPost = postServiceImpl.save(post);
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
        postServiceImpl.save(post);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }


    //UPDATE is only available for content owner
    @PutMapping("/post")
    //@P annotated args are marked as parameters and then can be accessed by security
    //@RA means context is calling for resourceAccess bean
    @PreAuthorize("@RA.checkIfPostOwner(#user, #post.getId())")
    public  ResponseEntity<Post> updatePost(@AuthenticationPrincipal @P("user") User user,
                              @RequestBody @P("post") PostUpdateDTO postUpdateDTO) {

        Post savedPost = postServiceImpl.update(postUpdateDTO);
        return new ResponseEntity<>(savedPost, HttpStatus.OK);
    }

    @PutMapping("/comment")
    @PreAuthorize("@RA.checkIfCommentOwner(#user, #comment.getId())")
    public ResponseEntity<Comment> updateComment(@AuthenticationPrincipal @P("user") User user,
                              @RequestBody @P("comment") CommentUpdateDTO commentUpdateDTO) {

        Comment savedComment = commentServiceImpl.update(commentUpdateDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.OK);
    }

    //DELETE mappings are available either to content owner or Role with content:delete
    @DeleteMapping("/post/{postId}")
    @PreAuthorize("@RA.checkIfPostOwner(#user, #postId) || hasAuthority('content:delete')")
    public ResponseEntity<HttpResponse> deletePost(@AuthenticationPrincipal @P("user") User user,
                                                   @PathVariable @P("postId") String postId) {

        postServiceImpl.deleteById(Long.valueOf(postId));
        log.info("Post of id={} deleted", postId);
        return HttpResponse.createResponseEntity(HttpStatus.OK, "Post successfully deleted");
    }

    @DeleteMapping("/comment/{commentId}")
    @PreAuthorize("@RA.checkIfCommentOwner(#user, #commentId) || hasAuthority('content:delete')")
    public ResponseEntity<HttpResponse> deleteComment(@AuthenticationPrincipal @P("user") User user,
                                                   @PathVariable @P("commentId") String commentId) {

        commentServiceImpl.deleteById(Long.valueOf(commentId));
        log.info("Comment of id={} deleted", commentId);
        return HttpResponse.createResponseEntity(HttpStatus.OK, "Comment successfully deleted");
    }

    @GetMapping("category/{categoryId}/follow")
    public ResponseEntity<HttpResponse> followCategory(@PathVariable Long categoryId, @AuthenticationPrincipal User user)
            throws ResourceNotFoundException {
        Category cat = categoryServiceImpl.findById(categoryId);
        if(user != null && cat != null) {
            user.getFollowedCategories().add(cat);
            userService.save(user);

            return HttpResponse.createResponseEntity(HttpStatus.OK, "Category followed");
        }
        throw new ResourceNotFoundException(String.format("Category of id=%d not found", categoryId));
    }

    @GetMapping("category/{categoryId}/unfollow")
    public ResponseEntity<HttpResponse> unfollowCategory(@PathVariable Long categoryId, @AuthenticationPrincipal User user)
            throws ResourceNotFoundException {
        Category cat = categoryServiceImpl.findById(categoryId);
        if(user != null && cat != null) {
            user.getFollowedCategories().remove(cat);
            userService.save(user);

            return HttpResponse.createResponseEntity(HttpStatus.OK, "Category unfollowed");
        }

        throw new ResourceNotFoundException(String.format("Category of id=%d not found", categoryId));
    }

}
