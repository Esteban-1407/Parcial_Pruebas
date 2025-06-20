package org.esteban.springboot.springmvc.app.springbootbackend;

import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.ProductRepository;
import org.esteban.springboot.springmvc.app.springbootbackend.services.Product.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setNombre("Producto 1");
        product1.setPrecio(100.0);
        product1.setStock(50);
        product1.setSku("SKU001");

        product2 = new Product();
        product2.setId(2L);
        product2.setNombre("Producto 2");
        product2.setPrecio(200.0);
        product2.setStock(30);
        product2.setSku("SKU002");
    }

    @Test
    void testFindAll_ShouldReturnAllProducts() {
        // Given
        List<Product> expectedProducts = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // When
        List<Product> actualProducts = productService.findAll();

        // Then
        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        assertEquals(expectedProducts, actualProducts);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById_WhenProductExists_ShouldReturnProduct() {
        // Given
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));

        // When
        Optional<Product> result = productService.findById(productId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(product1, result.get());
        assertEquals("Producto 1", result.get().getNombre());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testDeleteById_WhenProductExists_ShouldDeleteAndReturnProduct() {
        // Given
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));

        // When
        Optional<Product> result = productService.deleteById(productId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(product1, result.get());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteById_WhenProductDoesNotExist_ShouldReturnEmptyOptional() {
        // Given
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productService.deleteById(productId);

        // Then
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).deleteById(productId);
    }
}
