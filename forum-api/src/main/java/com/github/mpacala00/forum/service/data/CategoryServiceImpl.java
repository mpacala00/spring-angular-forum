package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.exception.model.ResourceNotFoundException;
import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.dto.category.CategoryDTO;
import com.github.mpacala00.forum.model.dto.category.CategoryPostsDTO;
import com.github.mpacala00.forum.repository.CategoryRepository;
import com.github.mpacala00.forum.service.dto.CategoryDTOMappingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryDTOMappingService categoryDTOMappingService;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryDTOMappingService::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Category findById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Category of id=%d does not exist", id));
        }

        return categoryOptional.get();
    }

    @Override
    public CategoryPostsDTO findByIdMapToDTO(Long id) {
        return categoryDTOMappingService.convertToCategoryPostsDTO(findById(id));
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
}
