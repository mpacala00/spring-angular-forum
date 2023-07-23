package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.exception.model.ResourceNotFoundException;
import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.UserLikedPost;
import com.github.mpacala00.forum.repository.PostRepository;
import com.github.mpacala00.forum.repository.UserLikedPostRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserLikedPostServiceImpl implements UserLikedPostService {

    PostRepository postRepository;
    UserLikedPostRepository userLikedPostRepository;

    @Override
    public boolean likeComment(User user, Long postId, Boolean isLike) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Post of id=%s not found", postId));
        }

        Optional<UserLikedPost> userLikedPostOpt = userLikedPostRepository
                .findByUserIdAndPostId(user.getId(), postId);

        if (userLikedPostOpt.isEmpty()) {
            userLikedPostRepository.save(new UserLikedPost(user, postOpt.get(), isLike));
            return true;
        }

        UserLikedPost userLikedPost = userLikedPostOpt.get();
        if (userLikedPost.getIsLike().equals(isLike)) {
            //if isLike's are equal remove existing reaction to comment
            userLikedPostRepository.delete(userLikedPost);
            return false;
        }

        //update reaction to the comment: like -> dislike || dislike -> like
        userLikedPost.setIsLike(isLike);
        userLikedPostRepository.save(userLikedPost);

        return false;
    }
}
