package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.UserLikedComment;
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

    @Override
    @Transactional
    public boolean likeComment(User user, Comment comment, Boolean isLike) {
        Optional<UserLikedComment> userLikedCommentOpt = userLikedCommentRepository
                .findByUserIdAndCommentId(user.getId(), comment.getId());

        if (userLikedCommentOpt.isEmpty()) {
            userLikedCommentRepository.save(new UserLikedComment(user, comment, isLike));
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
