package com.github.mpacala00.forum.repository;

import com.github.mpacala00.forum.model.UserLikedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLikedPostRepository extends JpaRepository<UserLikedPost, Long> {
    Optional<UserLikedPost> findByUserIdAndPostId(Long userId, Long postId);
    void deleteByPostId(Long postId);
}
