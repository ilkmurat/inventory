package com.murat.oneamz.inventory.controller;

import com.murat.oneamz.inventory.event.InventoryEventService;
import com.murat.oneamz.inventory.model.dto.ProductDTO;
import com.murat.oneamz.inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {
    private final ProductService productService;
    private final InventoryEventService inventoryEventService;

    public ProductController(ProductService productService, InventoryEventService inventoryEventService) {
        this.productService = productService;
        this.inventoryEventService = inventoryEventService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO product) {
        ProductDTO savedProduct = productService.saveProduct(product);
        inventoryEventService.publishEvent("Product created: " + product.getName());
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products in the inventory")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of products")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Optional<ProductDTO> product = Optional.ofNullable(productService.getProductById(id));
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) {
        Optional<ProductDTO> existingProduct = Optional.ofNullable(productService.getProductById(id));
        if (existingProduct.isPresent()) {
            product.setId(id);
            ProductDTO updatedProduct = productService.saveProduct(product);
            inventoryEventService.publishEvent("Product updated: " + product.getName());
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        inventoryEventService.publishEvent("Product deleted with ID: " + id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/discount/{category}")
    public ResponseEntity<List<ProductDTO>> applyDiscountToCategory(
            @PathVariable Long category,
            @RequestParam double discountPercentage) {
        List<ProductDTO> discountedProducts = productService.applyDiscountToCategory(category, discountPercentage);
        return ResponseEntity.ok(discountedProducts);
    }
}
