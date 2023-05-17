package com.github.mpacala00.forum.service.dto;

import com.github.mpacala00.forum.model.Comment;
import com.github.mpacala00.forum.model.UserLikedComment;
import com.github.mpacala00.forum.model.dto.comment.CommentDTO;
import com.github.mpacala00.forum.model.dto.comment.CommentPostDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentDTOMappingService implements DTOMappingService<Comment, CommentDTO>{

    @Override
    public CommentDTO convertToDTO(Comment entity) {
        CommentDTO dto = new CommentDTO();
        dto.setBody(entity.getBody());
        dto.setId(entity.getId());
        dto.setCreator(entity.getCreator().getUsername());
        dto.setPostDate(entity.getPostDate().toString());
        dto.setDeleted(entity.getDeleted());
        dto.setLikeCount(entity.getLikeCount());

        List<CommentDTO> childComments = entity.getChildComments()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        dto.setChildComments(childComments);

        return dto;
    }

    public CommentDTO convertToDTO(Comment entity, Long userId) {
        CommentDTO dto = new CommentDTO();
        dto.setBody(entity.getBody());
        dto.setId(entity.getId());
        dto.setCreator(entity.getCreator().getUsername());
        dto.setPostDate(entity.getPostDate().toString());
        dto.setDeleted(entity.getDeleted());
        dto.setLikeCount(entity.getLikeCount());
        dto.setIsLikedByUser(isLikedByUser(entity, userId));

        List<CommentDTO> childComments = entity.getChildComments()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        dto.setChildComments(childComments);

        return dto;
    }


    public Boolean isLikedByUser(Comment comment, Long userId) {
        Optional<Boolean> likingUserOpt = comment.getUserLikes().stream()
                .filter(userLikedComment -> userLikedComment.getUser().getId().equals(userId))
                .findAny()
                .map(UserLikedComment::getIsLike);

        return likingUserOpt.orElse(null);
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
