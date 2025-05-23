package org.esteban.springboot.springmvc.app.springbootbackend;

import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        Product product = new Product();
        product.setName("Te de Manzanilla");
        product.setDescription("Infusión natural");
        product.setPrice(9000L);
        productRepository.save(product);
    }

    @Test
    void shouldReturnProductById() throws Exception {
        // Convertir Iterable a List
        List<Product> products = StreamSupport
                .stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        Product product = products.get(0); // Acceder al primer producto insertado

        mockMvc.perform(get("/products/{id}", product.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Te de Manzanilla"))
                .andExpect(jsonPath("$.price").value(9000));
    }
}
