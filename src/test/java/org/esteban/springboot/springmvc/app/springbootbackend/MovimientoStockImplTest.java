package org.esteban.springboot.springmvc.app.springbootbackend;

import org.esteban.springboot.springmvc.app.springbootbackend.entities.MovimientoStock;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.MovimientoStockRepository;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.ProductRepository;
import org.esteban.springboot.springmvc.app.springbootbackend.services.MovimientoStock.MovimientoStockImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoStockImplTest {

    @Mock
    private MovimientoStockRepository movimientoStockRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MovimientoStockImpl movimientoStockService;

    private Product product;
    private MovimientoStock movimientoEntrada;
    private MovimientoStock movimientoSalida;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setNombre("Producto Test");
        product.setPrecio(100.0);
        product.setStock(50);
        product.setSku("SKU001");

        movimientoEntrada = new MovimientoStock();
        movimientoEntrada.setId(1L);
        movimientoEntrada.setProducto(product);
        movimientoEntrada.setTipoMovimiento(MovimientoStock.TipoMovimiento.ENTRADA);
        movimientoEntrada.setCantidad(10);
        movimientoEntrada.setMotivo("Compra");

        movimientoSalida = new MovimientoStock();
        movimientoSalida.setId(2L);
        movimientoSalida.setProducto(product);
        movimientoSalida.setTipoMovimiento(MovimientoStock.TipoMovimiento.SALIDA);
        movimientoSalida.setCantidad(5);
        movimientoSalida.setMotivo("Venta");
    }

    @Test
    void testRegistrarEntrada_ShouldCreateEntradaMovimientoAndUpdateStock() {
        // Given
        Long productoId = 1L;
        Integer cantidad = 20;
        String motivo = "Reposición de inventario";
        Integer stockInicial = product.getStock();

        when(productRepository.findById(productoId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movimientoStockRepository.save(any(MovimientoStock.class))).thenReturn(movimientoEntrada);

        // When
        MovimientoStock result = movimientoStockService.registrarEntrada(productoId, cantidad, motivo);

        // Then
        assertNotNull(result);

        // Verificar que el stock del producto se actualizó correctamente
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product productSaved = productCaptor.getValue();
        assertEquals(stockInicial + cantidad, productSaved.getStock());

        // Verificar que se guardó el movimiento correcto
        ArgumentCaptor<MovimientoStock> movimientoCaptor = ArgumentCaptor.forClass(MovimientoStock.class);
        verify(movimientoStockRepository).save(movimientoCaptor.capture());
        MovimientoStock movimientoSaved = movimientoCaptor.getValue();

        assertEquals(product, movimientoSaved.getProducto());
        assertEquals(MovimientoStock.TipoMovimiento.ENTRADA, movimientoSaved.getTipoMovimiento());
        assertEquals(cantidad, movimientoSaved.getCantidad());
        assertEquals(motivo, movimientoSaved.getMotivo());

        verify(productRepository, times(1)).findById(productoId);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(movimientoStockRepository, times(1)).save(any(MovimientoStock.class));
    }

    @Test
    void testRegistrarSalida_WithSufficientStock_ShouldCreateSalidaMovimientoAndUpdateStock() {
        // Given
        Long productoId = 1L;
        Integer cantidad = 15;
        String motivo = "Venta al cliente";
        Integer stockInicial = product.getStock(); // 50

        when(productRepository.findById(productoId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(movimientoStockRepository.save(any(MovimientoStock.class))).thenReturn(movimientoSalida);

        // When
        MovimientoStock result = movimientoStockService.registrarSalida(productoId, cantidad, motivo);

        // Then
        assertNotNull(result);

        // Verificar que el stock del producto se actualizó correctamente
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product productSaved = productCaptor.getValue();
        assertEquals(stockInicial - cantidad, productSaved.getStock());

        // Verificar que se guardó el movimiento correcto
        ArgumentCaptor<MovimientoStock> movimientoCaptor = ArgumentCaptor.forClass(MovimientoStock.class);
        verify(movimientoStockRepository).save(movimientoCaptor.capture());
        MovimientoStock movimientoSaved = movimientoCaptor.getValue();

        assertEquals(product, movimientoSaved.getProducto());
        assertEquals(MovimientoStock.TipoMovimiento.SALIDA, movimientoSaved.getTipoMovimiento());
        assertEquals(cantidad, movimientoSaved.getCantidad());
        assertEquals(motivo, movimientoSaved.getMotivo());

        verify(productRepository, times(1)).findById(productoId);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(movimientoStockRepository, times(1)).save(any(MovimientoStock.class));
    }

    // ========== PRUEBAS DE VALIDACIÓN DE ENTRADAS ==========

    @Test
    void testRegistrarEntrada_WithNonExistentProduct_ShouldThrowRuntimeException() {
        // Given
        Long productoIdInexistente = 999L;
        Integer cantidad = 10;
        String motivo = "Compra";

        when(productRepository.findById(productoIdInexistente)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movimientoStockService.registrarEntrada(productoIdInexistente, cantidad, motivo);
        });

        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productRepository, times(1)).findById(productoIdInexistente);
        verify(productRepository, never()).save(any(Product.class));
        verify(movimientoStockRepository, never()).save(any(MovimientoStock.class));
    }

    @Test
    void testRegistrarSalida_WithInsufficientStock_ShouldThrowRuntimeException() {
        // Given
        Long productoId = 1L;
        Integer cantidadExcesiva = 100; // Mayor al stock disponible (50)
        String motivo = "Venta";

        when(productRepository.findById(productoId)).thenReturn(Optional.of(product));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movimientoStockService.registrarSalida(productoId, cantidadExcesiva, motivo);
        });

        assertEquals("Stock insuficiente", exception.getMessage());
        verify(productRepository, times(1)).findById(productoId);
        verify(productRepository, never()).save(any(Product.class));
        verify(movimientoStockRepository, never()).save(any(MovimientoStock.class));
    }
}
