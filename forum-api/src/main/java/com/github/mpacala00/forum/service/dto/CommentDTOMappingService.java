package com.github.mpacala00.forum.service.dto;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.dto.comment.CommentDTO;
import com.github.mpacala00.forum.model.dto.comment.CommentPostDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentDTOMappingService implements DTOMappingService<Comment, CommentDTO>{

    @Override
    public CommentDTO convertToDTO(Comment entity) {
        CommentDTO dto = new CommentDTO();
        dto.setBody(entity.getBody());
        dto.setId(entity.getId());
        dto.setCreator(entity.getCreator().getUsername());
        dto.setPostDate(entity.getPostDate().toString());
        return dto;
    }

    @Override
    public Comment convertToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setBody(commentDTO.getBody());
        comment.setId(commentDTO.getId());
        comment.setPostDate(LocalDateTime.parse(commentDTO.getPostDate()));
        return comment;
    }

    //include Post ID and Post title
    public CommentPostDTO convertToCommentPostDTO(Comment entity) {
        CommentPostDTO dto = new CommentPostDTO();
        dto.setBody(entity.getBody());
        dto.setId(entity.getId());
        dto.setCreator(entity.getCreator().getUsername());
        dto.setPostDate(entity.getPostDate().toString());

        dto.setPostId(entity.getPost().getId());
        dto.setPostTitle(entity.getPost().getTitle());
        dto.setPostCategoryId(entity.getPost().getCategory().getId());
        return dto;
    }
}
