package com.murat.oneamz.inventory.service;

import com.murat.oneamz.inventory.exceptions.ItemNotFoundException;
import com.murat.oneamz.inventory.model.Category;
import com.murat.oneamz.inventory.model.dto.CategoryDTO;
import com.murat.oneamz.inventory.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    // Convert DTO to Entity
    private Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return category;
    }


    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        return convertToDTO(category); // Convert Entity to DTO
    }

    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        categoryRepository.save(category);
        return convertToDTO(category);
    }

    public void deleteCategory(Long id) {
        Category product = categoryRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        categoryRepository.delete(product);
    }
}


