package com.murat.oneamz.inventory.controller;

import com.murat.oneamz.inventory.event.InventoryEventService;
import com.murat.oneamz.inventory.model.dto.CategoryDTO;
import com.murat.oneamz.inventory.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final InventoryEventService inventoryEventService;
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService, InventoryEventService inventoryEventService) {
        this.categoryService = categoryService;
        this.inventoryEventService = inventoryEventService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<CategoryDTO> category = Optional.ofNullable(categoryService.getCategoryById(id));
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO category) {
        CategoryDTO savedCategory = categoryService.saveCategory(category);
        inventoryEventService.publishEvent("Category created: " + category.getName());
        return ResponseEntity.ok(savedCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO category) {
        Optional<CategoryDTO> existingCategory = Optional.ofNullable(categoryService.getCategoryById(id));
        if (existingCategory.isPresent()) {
            category.setId(id);
            CategoryDTO updatedCategory = categoryService.saveCategory(category);
            inventoryEventService.publishEvent("Category updated: " + category.getName());
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        inventoryEventService.publishEvent("Category deleted with ID: " + id);
        return ResponseEntity.noContent().build();
    }
}
