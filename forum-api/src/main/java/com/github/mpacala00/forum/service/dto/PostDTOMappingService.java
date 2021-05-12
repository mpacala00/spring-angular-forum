package com.github.mpacala00.forum.service.dto;

import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.dto.PostDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostDTOMappingService implements DTOMappingService<Post, PostDTO> {

    @Override
    public PostDTO convertToDTO(Post entity) {
        PostDTO dto = new PostDTO();
        dto.setBody(entity.getBody());
        dto.setId(entity.getId());
        dto.setCategoryId(entity.getCategory().getId());
        dto.setPostDate(entity.getPostDate().toString());
        dto.setTitle(entity.getTitle());
        //only username will be needed to represent the user object
        dto.setCreator(entity.getCreator().getUsername());
        return dto;
    }

    @Override
    public Post convertToEntity(PostDTO postDTO) {
        Post post = new Post();
        post.setPostDate(LocalDateTime.parse(postDTO.getPostDate()));
        post.setBody(postDTO.getBody());
        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        return post;
    }
}
