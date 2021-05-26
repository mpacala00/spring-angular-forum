package com.github.mpacala00.forum.service.dto;

import com.github.mpacala00.forum.model.Category;
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
    public CategoryPostsDTO convertToCategoryPostsDTO(Category entity) {
        CategoryPostsDTO dto = new CategoryPostsDTO();
        dto.setDescription(entity.getDescription());
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        dto.setPosts(entity.getPosts()
                .stream()
                .map(postDTOMappingService::convertToDTO)
                .collect(Collectors.toList()));
        return dto;
    }

}
