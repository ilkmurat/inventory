package com.murat.oneamz.inventory;

import com.murat.oneamz.inventory.model.Category;
import com.murat.oneamz.inventory.model.dto.CategoryDTO;
import com.murat.oneamz.inventory.repository.CategoryRepository;
import com.murat.oneamz.inventory.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testSaveCategory() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Test Category");

        Category category = new Category();
        category.setName(categoryDTO.getName());

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO saveCategory = categoryService.saveCategory(categoryDTO);
        assertNotNull(saveCategory);
        assertEquals("Test Category", saveCategory.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testGetCategoryById() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDTO foundCategory = categoryService.getCategoryById(1L);
        assertNotNull(foundCategory);
        assertEquals("Test Category", foundCategory.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllCategories() {
        Category category1 = new Category();
        category1.setName("Product 1");

        Category category2 = new Category();
        category2.setName("Product 2");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<CategoryDTO> products = categoryService.getAllCategories();
        assertNotNull(products);
        assertEquals(2, products.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testDeleteProduct() {
        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
