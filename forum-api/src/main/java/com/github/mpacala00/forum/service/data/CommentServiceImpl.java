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
import com.github.mpacala00.forum.repository.UserRepository;
import com.github.mpacala00.forum.service.dto.CommentDTOMappingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentDTOMappingService commentDTOMappingService;

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
    public Comment update(CommentUpdateDTO commment) {
        if(commentRepository.findById(commment.getId()).isPresent()) {
            Comment commentToUpdate = commentRepository.findById(commment.getId()).get();
            commentToUpdate.setBody(commment.getBody());
            commentToUpdate.setPostDate(LocalDateTime.now());
            return commentRepository.save(commentToUpdate);
        }
        throw new NullPointerException(String.format("Comment of id=%d does not exist", commment.getId()));
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
