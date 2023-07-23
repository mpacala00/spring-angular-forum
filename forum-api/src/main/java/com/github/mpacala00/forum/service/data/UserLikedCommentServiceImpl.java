package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.exception.model.ResourceNotFoundException;
import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.UserLikedComment;
import com.github.mpacala00.forum.repository.CommentRepository;
import com.github.mpacala00.forum.repository.UserLikedCommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserLikedCommentServiceImpl implements UserLikedCommentService {

    UserLikedCommentRepository userLikedCommentRepository;
    CommentRepository commentRepository;

    @Override
    @Transactional
    public boolean likeComment(User user, Long commentId, Boolean isLike) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Comment of id=%s not found", commentId));
        }

        Optional<UserLikedComment> userLikedCommentOpt = userLikedCommentRepository
                .findByUserIdAndCommentId(user.getId(), commentId);

        if (userLikedCommentOpt.isEmpty()) {
            userLikedCommentRepository.save(new UserLikedComment(user, commentOpt.get(), isLike));
            return true;
        }

        UserLikedComment userLikedComment = userLikedCommentOpt.get();
        if (userLikedComment.getIsLike().equals(isLike)) {
            //if isLike's are equal remove existing reaction to comment
            userLikedCommentRepository.delete(userLikedComment);
            return false;
        }

        //update reaction to the comment: like -> dislike || dislike -> like
        userLikedComment.setIsLike(isLike);
        userLikedCommentRepository.save(userLikedComment);

        return false;
    }
}
