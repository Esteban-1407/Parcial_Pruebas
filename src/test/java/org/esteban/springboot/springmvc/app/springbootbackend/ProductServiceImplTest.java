package org.esteban.springboot.springmvc.app.springbootbackend;


import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.ProductRepository;
import org.esteban.springboot.springmvc.app.springbootbackend.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    private ProductRepository productRepository;
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void save_shouldReturnSavedProduct() {
        Product product = new Product();
        product.setName("Café Premium");
        product.setDescription("Paquete 500g");
        product.setPrice(15000L);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.save(product);

        assertNotNull(savedProduct);
        assertEquals("Café Premium", savedProduct.getName());
        verify(productRepository, times(1)).save(product);
    }
}

