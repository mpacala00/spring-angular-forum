package com.github.mpacala00.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.github.mpacala00.forum.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCreator(String creator); //creator - have to match variable name in Post class
    //<type, id>


}
