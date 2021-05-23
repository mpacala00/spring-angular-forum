package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Post;

import java.util.List;
import java.util.Set;

public interface PostService {

    Set<Post> getAllPosts();
    Post savePost(Post post);
    Post findById(Long id);
}
