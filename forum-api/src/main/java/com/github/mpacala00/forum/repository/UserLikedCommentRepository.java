package com.github.mpacala00.forum.repository;

import com.github.mpacala00.forum.model.UserLikedComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLikedCommentRepository extends JpaRepository<UserLikedComment, Long> {

    Optional<UserLikedComment> findByUserIdAndCommentId(Long userId, Long commentId);
}
