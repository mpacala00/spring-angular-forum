package com.github.mpacala00.forum.service.dto;

import com.github.mpacala00.forum.model.Post;
import com.github.mpacala00.forum.model.UserLikedPost;
import com.github.mpacala00.forum.model.dto.post.PostDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
        dto.setLikeCount(entity.getLikeCount());
        //only username will be needed to represent the user object
        dto.setCreator(entity.getCreator().getUsername());
        return dto;
    }

    public PostDTO convertToDTO(Post entity, Long userId) {
        PostDTO dto = new PostDTO();
        dto.setBody(entity.getBody());
        dto.setId(entity.getId());
        dto.setCategoryId(entity.getCategory().getId());
        dto.setPostDate(entity.getPostDate().toString());
        dto.setTitle(entity.getTitle());
        dto.setLikeCount(entity.getLikeCount());
        dto.setIsLikedByUser(isLikedByUser(entity, userId));

        dto.setCreator(entity.getCreator().getUsername());
        return dto;
    }

    public Boolean isLikedByUser(Post post, Long userId) {
        Optional<Boolean> likingUserOpt = post.getUserLikes().stream()
                .filter(userLikedPost -> userLikedPost.getUser().getId().equals(userId))
                .findAny()
                .map(UserLikedPost::getIsLike);

        return likingUserOpt.orElse(null);
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
