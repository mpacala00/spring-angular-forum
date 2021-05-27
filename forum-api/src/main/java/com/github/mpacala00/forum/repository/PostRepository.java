package com.github.mpacala00.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.github.mpacala00.forum.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCreator(String creator);

    //custom query is necessary because entity was being removed from repo
    //but not from the actual database
    @Modifying
    @Query(value = "DELETE FROM post WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);

}
