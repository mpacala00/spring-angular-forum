package com.github.mpacala00.forum.service;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class CategoryService {

    CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        if(categoryRepository.findById(id).isEmpty()) {
            throw new NullPointerException(String.format("Category of id=%d does not exist", id));
        }
        return categoryRepository.findById(id).get();
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }
}
