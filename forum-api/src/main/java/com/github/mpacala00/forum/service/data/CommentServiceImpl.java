package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.exception.model.ResourceNotFoundException;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.UserLikedComment;
import com.github.mpacala00.forum.model.constant.CommentConstants;
import com.github.mpacala00.forum.model.dto.comment.CommentDTO;
import com.github.mpacala00.forum.model.dto.comment.CommentUpdateDTO;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.repository.PostRepository;
import com.github.mpacala00.forum.service.dto.CommentDTOMappingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    PostRepository postRepository;
    CommentDTOMappingService commentDTOMappingService;

    @Override
    public List<CommentDTO> getAllCommentsFromPost(Long postId, Long tokenUserId) {
        List<Comment> postComments = commentRepository.findByPostId(postId);
        if (postComments.size() == 0) {
            return Collections.emptyList();
        }

        return mapCommentsToDto(postComments, tokenUserId);
    }

    private List<CommentDTO> mapCommentsToDto(List<Comment> comments, Long userId) {
        if (userId == null) {
            return comments.stream()
                    .filter(comment -> comment.getParentComment() == null)
                    .map(commentDTOMappingService::convertToDTO)
                    .collect(Collectors.toList());
        }

        return comments.stream()
                .filter(comment -> comment.getParentComment() == null)
                .map(comment -> commentDTOMappingService.convertToDTO(comment, userId))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentDto(Long commentId, Long tokenUserId) throws ResourceNotFoundException {
        Comment comment = findById(commentId);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment of id=" + commentId + "not found.");
        }

        return commentDTOMappingService.convertToDTO(comment, tokenUserId);
    }

    @Override //todo - using on a list of comments can be slow
    public Boolean isLikedByUser(Comment comment, Long userId) {
        Optional<Boolean> likingUserOpt = comment.getUserLikes().stream()
                .filter(userLikedComment -> userLikedComment.getUser().getId().equals(userId))
                .findAny()
                .map(UserLikedComment::getIsLike);

        return likingUserOpt.orElse(null);
    }

    @Override
    public Set<Comment> findByUser(String user) {
        return new HashSet<>(commentRepository.findByCreator(user));
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment saveCommentInPost(Comment comment, Long postId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Post of id=%s not found", postId));
        }

        Comment savedComment = save(comment);

        Post post = postOpt.get();
        post.addComment(savedComment);
        postRepository.save(post);

        return savedComment;
    }

    @Override
    public Comment replyToComment(Comment comment, Long postId, Long parentCommentId) {
        Optional<Comment> parentCommentOpt = commentRepository.findById(parentCommentId);
        if (parentCommentOpt.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Comment you are replying (id=%s) to does not exist",
                    parentCommentId));
        }

        Comment parentComment = parentCommentOpt.get();
        if (parentComment.getDeleted()) {
            throw new RuntimeException("Replying to deleted comments is forbidden");
        }

        comment.setParentComment(parentComment);

        return saveCommentInPost(comment, postId);
    }

    @Override
    public Comment update(CommentUpdateDTO commment, Long commentId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Comment of id=%d does not exist", commment.getId()));
        }

        Comment commentToUpdate = commentOpt.get();
        commentToUpdate.setBody(commment.getBody());
        commentToUpdate.setPostDate(LocalDateTime.now());

        return commentRepository.save(commentToUpdate);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Comment commentToDelete = findById(id);
        if (commentToDelete.getDeleted()) {
            return;
        }

        if (CollectionUtils.isEmpty(commentToDelete.getChildComments())) {
            commentToDelete.setPost(null);
            commentToDelete.setCreator(null);

            commentRepository.deleteById(id);
        } else {
            commentToDelete.setBody(CommentConstants.DELETED_BODY.getValue());
            commentToDelete.setDeleted(true);

            commentRepository.save(commentToDelete);
        }
    }
}
