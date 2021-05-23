package com.github.mpacala00.forum.service.data;

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
public class CategoryServiceImpl implements CategoryService{

    CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        if(categoryRepository.findById(id).isEmpty()) {
            throw new NullPointerException(String.format("Category of id=%d does not exist", id));
        }
        return categoryRepository.findById(id).get();
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
}
