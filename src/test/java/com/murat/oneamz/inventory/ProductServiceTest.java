package com.murat.oneamz.inventory;

import com.murat.oneamz.inventory.model.Category;
import com.murat.oneamz.inventory.model.Product;
import com.murat.oneamz.inventory.model.dto.ProductDTO;
import com.murat.oneamz.inventory.repository.CategoryRepository;
import com.murat.oneamz.inventory.repository.ProductRepository;
import com.murat.oneamz.inventory.service.ProductService;
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

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        // product = new ProductDTO("Laptop", "Gaming Laptop", 1500.0, 5, null);
    }

    @Test
    void testSaveProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setPrice(100.0);
        productDTO.setQuantity(10);
        productDTO.setCategoryName("Book");

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(categoryRepository.findByName(any())).thenReturn(new Category());

        ProductDTO savedProduct = productService.saveProduct(productDTO);
        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setPrice(200.0);
        productDTO.setQuantity(20);

        Product product = new Product();
        product.setId(1L);
        product.setName("Old Product");
        product.setPrice(100.0);
        product.setQuantity(10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(categoryRepository.findByName(any())).thenReturn(new Category());

        ProductDTO updatedProduct = productService.saveProduct(productDTO);
        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setQuantity(1);
        product.setPrice(10.0);
        product.setCategory(new Category());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName(any())).thenReturn(new Category());
        ProductDTO foundProduct = productService.getProductById(1L);
        assertNotNull(foundProduct);
        assertEquals("Test Product", foundProduct.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setQuantity(1);
        product1.setPrice(10.0);
        product1.setCategory(new Category());

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setQuantity(1);
        product2.setPrice(10.0);
        product2.setCategory(new Category());

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        when(categoryRepository.findByName(any())).thenReturn(new Category());
        List<ProductDTO> products = productService.getAllProducts();
        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }
}
