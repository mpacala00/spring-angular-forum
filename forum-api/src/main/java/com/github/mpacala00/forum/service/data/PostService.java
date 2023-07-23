package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.dto.post.PostDTO;
import com.github.mpacala00.forum.model.dto.post.PostUpdateDTO;

import java.util.List;

public interface PostService {

    Post save(Post post);
    Post savePostInCategory(Post post, Long categoryId);
    Post update(PostUpdateDTO post, Long postId);
    Post findById(Long id);
    List<Post> findByCategoryId(Long categoryId);
    List<PostDTO> findByCategoryIdMapToDTO(Long categoryId);
    void deleteById(Long id);
}
