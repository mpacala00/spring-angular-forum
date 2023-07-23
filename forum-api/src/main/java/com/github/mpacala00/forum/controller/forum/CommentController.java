package com.github.mpacala00.forum.controller.forum;

import com.github.mpacala00.forum.exception.model.ResourceNotFoundException;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.dto.comment.CommentDTO;
import com.github.mpacala00.forum.model.dto.comment.CommentUpdateDTO;
import com.github.mpacala00.forum.pojos.HttpResponse;
import com.github.mpacala00.forum.security.UserAuthenticationService;
import com.github.mpacala00.forum.service.data.CommentService;
import com.github.mpacala00.forum.service.data.UserLikedCommentService;
import com.github.mpacala00.forum.service.dto.CommentDTOMappingService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RestController
@RequestMapping("/categories/{categoryId}/posts/{postId}/comments")
public class CommentController {

    CommentService commentService;
    UserLikedCommentService userLikedCommentService;
    CommentDTOMappingService commentDTOMappingService;
    UserAuthenticationService userAuthenticationService;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@RequestHeader Map<String, String> headers,
            @PathVariable Long postId) {
        Optional<User> userOpt = userAuthenticationService.retrieveByRequestHeadersToken(headers);
        List<CommentDTO> comments = commentService.getAllCommentsFromPost(postId,
                userOpt.map(User::getId).orElse(null));

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Comment> publishComment(@PathVariable String postId,
            @RequestParam(required = false) Optional<Long> replyTo,
            @RequestBody Comment comment, @AuthenticationPrincipal User user) {
        comment.setCreator(user); //todo handle on front-end

        Comment savedComment = null;
        if (replyTo.isPresent()) {
            savedComment = commentService.replyToComment(comment, Long.valueOf(postId), replyTo.get());
        } else {
            savedComment = commentService.saveCommentInPost(comment, Long.valueOf(postId));
        }

        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("@RA.checkIfCommentOwner(#user, #comment.getId())")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId,
            @AuthenticationPrincipal @P("user") User user,
            @RequestBody @P("comment") CommentUpdateDTO commentUpdateDTO) {
        Comment savedComment = commentService.update(commentUpdateDTO, commentId);

        return new ResponseEntity<>(savedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("@RA.checkIfCommentOwner(#user, #commentId) || hasAuthority('content:delete')")
    public ResponseEntity<HttpResponse> deleteComment(@AuthenticationPrincipal @P("user") User user,
                                                      @PathVariable @P("commentId") String commentId) {
        commentService.deleteById(Long.valueOf(commentId));

        return HttpResponse.createResponseEntity(HttpStatus.NO_CONTENT, "Comment successfully deleted");
    }

    @PutMapping("/{commentId}/like/{isLike}")
    public ResponseEntity<CommentDTO> likeComment(@PathVariable Long commentId, @PathVariable Boolean isLike,
                                                  @AuthenticationPrincipal User user) throws ResourceNotFoundException {
        userLikedCommentService.likeComment(user, commentId, isLike);

        //return comment with updated likeCount and isLikedByUser fields
        return new ResponseEntity<>(commentService.getCommentDto(commentId, user.getId()), HttpStatus.OK);
    }
}
