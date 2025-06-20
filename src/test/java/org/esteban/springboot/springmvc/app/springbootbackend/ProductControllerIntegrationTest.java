package org.esteban.springboot.springmvc.app.springbootbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.Categories;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.CategoryRepository;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/Prueba_Parcial",
        "spring.datasource.username=postgres",
        "spring.datasource.password=postgress",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class ProductControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Categories categoria;
    private Product product;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Limpiar base de datos
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        // Crear y GUARDAR categoría de prueba PRIMERO
        categoria = new Categories();
        categoria.setNombre("Electrónicos");
        categoria.setDescripcion("Productos electrónicos");
        categoria.setFechaCreacion(LocalDateTime.now());
        categoria.setFechaActualizacion(LocalDateTime.now());
        categoria = categoryRepository.save(categoria); // ← GUARDAR CATEGORÍA PRIMERO

        // Crear producto de prueba
        product = new Product();
        product.setNombre("Laptop HP");
        product.setPrecio(1500.00);
        product.setStock(10);
        product.setDescripcion("Laptop HP Pavilion 15");
        product.setSku("SKU-LAPTOP-001");
        product.setFechaCreacion(LocalDateTime.now());
        product.setFechaActualizacion(LocalDateTime.now());
        product.setCategoria(categoria); // ← Ahora la categoría ya está guardada
    }



    @Test
    void testGetNonExistentProduct_ShouldReturn404() throws Exception {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        mockMvc.perform(get("/products/" + nonExistentId))
                .andDo(print())
                .andExpect(status().isNotFound());

        // Verificar que no existe en base de datos
        assertFalse(productRepository.existsById(nonExistentId));
    }

    @Test
    void testDeleteProduct_ShouldRemoveFromDatabase() throws Exception {
        // Given - Crear producto en base de datos
        Product savedProduct = productRepository.save(product);
        Long productId = savedProduct.getId();

        // Verificar que existe antes de eliminar
        assertTrue(productRepository.existsById(productId));

        // When & Then - Eliminar producto
        mockMvc.perform(delete("/products/" + productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId.intValue())))
                .andExpect(jsonPath("$.nombre", is("Laptop HP")));

        // Verificar que fue eliminado de la base de datos
        assertFalse(productRepository.existsById(productId));
    }
}