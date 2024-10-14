package com.murat.oneamz.inventory.service;

import com.murat.oneamz.inventory.exceptions.BadRequestException;
import com.murat.oneamz.inventory.exceptions.ItemNotFoundException;
import com.murat.oneamz.inventory.model.Category;
import com.murat.oneamz.inventory.model.Product;
import com.murat.oneamz.inventory.model.dto.ProductDTO;
import com.murat.oneamz.inventory.repository.CategoryRepository;
import com.murat.oneamz.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {


    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // Convert Entity to DTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setCategoryName(product.getCategory().getName());
        return productDTO;
    }

    // Convert DTO to Entity
    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        Category category = categoryRepository.findByName(productDTO.getCategoryName());
        product.setCategory(category);
        return product;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        return convertToDTO(product); // Convert Entity to DTO
    }


    public ProductDTO saveProduct(ProductDTO productDTO) {
        validateProduct(productDTO);
        Product product = convertToEntity(productDTO);
        productRepository.save(product);
        return convertToDTO(product);
    }

    // Delete product by ID
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        productRepository.delete(product);
    }


    // Apply discount to products in a category and return as DTO
    public List<ProductDTO> applyDiscountToCategory(Long category, double discountPercentage) {
        List<Product> products = productRepository.findByCategoryId(category);

        return products.stream()
                .map(product -> {
                    double newPrice = product.getPrice() * (1 - discountPercentage / 100);
                    product.setPrice(newPrice); // Update discounted price
                    productRepository.save(product); // Update in the database
                    return convertToDTO(product); // Convert to DTO
                })
                .collect(Collectors.toList());
    }

    private void validateProduct(ProductDTO product) {
        if (product.getPrice() < 0) {
            throw new BadRequestException("Price cannot be negative.");
        }
    }
}
