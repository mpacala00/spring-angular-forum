package com.github.mpacala00.forum.service.dto;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.dto.category.CategoryDTO;
import com.github.mpacala00.forum.model.dto.category.CategoryPostsDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CategoryDTOMappingService implements DTOMappingService<Category, CategoryDTO> {

    private final PostDTOMappingService postDTOMappingService;

    @Override
    public CategoryDTO convertToDTO(Category entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setDescription(entity.getDescription());
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        dto.setUserFollowing(false);
        return dto;
    }

    @Override
    public Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setDescription(categoryDTO.getDescription());
        category.setName(categoryDTO.getName());
        return category;
    }

    //include post list in dto
    public CategoryPostsDTO convertToCategoryPostsDTO(Category entity, Long userId) {
        CategoryPostsDTO dto = new CategoryPostsDTO();

        dto.setDescription(entity.getDescription());
        dto.setName(entity.getName());
        dto.setId(entity.getId());

        if (userId != null) {
            boolean isUserFollowing = entity.getFollowingUsers().stream()
                    .map(User::getId)
                    .anyMatch(uid -> uid.equals(userId));
            dto.setUserFollowing(isUserFollowing);

            dto.setPosts(entity.getPosts()
                    .stream()
                    .map(post -> postDTOMappingService.convertToDTO(post, userId))
                    .collect(Collectors.toList()));
        } else {
            dto.setPosts(entity.getPosts()
                    .stream()
                    .map(postDTOMappingService::convertToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

}
