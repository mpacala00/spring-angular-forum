package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.dto.post.PostUpdateDTO;

import java.util.Set;

public interface PostService {

    Set<Post> getAllPosts();
    Post save(Post post);
    Post update(PostUpdateDTO post);
    Post findById(Long id);
}
