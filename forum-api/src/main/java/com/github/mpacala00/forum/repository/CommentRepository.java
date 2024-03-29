package com.github.mpacala00.forum.repository;

import com.github.mpacala00.forum.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCreator(String creator);

    List<Comment> findByPostId(Long postId);

    @Modifying
    @Query(value = "DELETE FROM comments WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
