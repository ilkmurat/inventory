package com.murat.oneamz.inventory;

import com.murat.oneamz.inventory.model.dto.ProductDTO;
import com.murat.oneamz.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setCategoryName("bilgisayar");
        productDTO.setPrice(99.99);
        productDTO.setQuantity(10);
    }

    @Test
    void testCreateProduct() throws Exception {
        String productJson = "{\"name\":\"New Product\", \"categoryName\":\"bilgisayar\", \"price\":99.99, \"quantity\":5}";

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Product")));
    }

    @Test
    void testGetProductById() throws Exception {
        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")));
    }

    @Test
    void testUpdateProduct() throws Exception {
        String updatedProductJson = "{\"name\":\"Updated Product\", \"categoryName\":\"Book\", \"price\":199.99, \"quantity\":20}";

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Product")));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

   /* @Test
    void testGetAllProducts() {
        // Act: Call the endpoint
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/products", Product[].class);

        // Assert: Verify the response
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void testCreateProduct() {
        Product product = new Product("Smartphone", "New Smartphone", 999.0, 15, null);

        // Act: Send POST request
        ResponseEntity<Product> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/products", product, Product.class);

        // Assert: Verify product creation
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Smartphone", responseEntity.getBody().getName());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product( "Tablet", "High-end tablet", 1200.0, 3, null);
        product = productRepository.save(product);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Product> entity = new HttpEntity<>(null, headers);

        // Act: Send DELETE request
        ResponseEntity<Void> responseEntity = restTemplate.exchange("http://localhost:" + port + "/products/" + product.getId(),
                HttpMethod.DELETE, entity, Void.class);

        // Assert: Verify product deletion
        assertEquals(204, responseEntity.getStatusCodeValue());
    }
    *
    */
}
