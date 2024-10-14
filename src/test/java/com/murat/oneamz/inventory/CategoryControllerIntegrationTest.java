package com.murat.oneamz.inventory;

import com.murat.oneamz.inventory.model.Category;
import com.murat.oneamz.inventory.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testGetAllCategories() {
        // Act: Call the endpoint
        ResponseEntity<Category[]> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/categories", Category[].class);

        // Assert: Verify the response
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setName("Test Create Category");

        // Act: Send POST request
        ResponseEntity<Category> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/categories", category, Category.class);

        // Assert: Verify product creation
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Test Create Category", responseEntity.getBody().getName());
    }

    @Test
    void testDeleteProduct() {
        Category category = new Category();
        category.setName("Test Delete Category");
        category = categoryRepository.save(category);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Category> entity = new HttpEntity<>(null, headers);

        // Act: Send DELETE request
        ResponseEntity<Void> responseEntity = restTemplate.exchange("http://localhost:" + port + "/categories/" + category.getId(),
                HttpMethod.DELETE, entity, Void.class);

        // Assert: Verify product deletion
        assertEquals(204, responseEntity.getStatusCodeValue());
    }
}
