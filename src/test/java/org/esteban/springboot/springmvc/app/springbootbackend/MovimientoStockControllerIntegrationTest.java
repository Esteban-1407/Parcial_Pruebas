package org.esteban.springboot.springmvc.app.springbootbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.esteban.springboot.springmvc.app.springbootbackend.dto.MovimientoStockRequestDTO;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.Categories;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.MovimientoStock;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.CategoryRepository;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.MovimientoStockRepository;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
class MovimientoStockControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MovimientoStockRepository movimientoStockRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Product product;
    private Categories categoria;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Limpiar base de datos
        movimientoStockRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        // Crear y GUARDAR categoría de prueba PRIMERO
        categoria = new Categories();
        categoria.setNombre("Electrónicos");
        categoria.setDescripcion("Productos electrónicos");
        categoria.setFechaCreacion(LocalDateTime.now());
        categoria.setFechaActualizacion(LocalDateTime.now());
        categoria = categoryRepository.save(categoria); // ← GUARDAR CATEGORÍA PRIMERO

        // Crear y guardar producto de prueba
        product = new Product();
        product.setNombre("Tablet Samsung");
        product.setPrecio(500.00);
        product.setStock(30);
        product.setDescripcion("Tablet Samsung Galaxy Tab A");
        product.setSku("SKU-TABLET-001");
        product.setFechaCreacion(LocalDateTime.now());
        product.setFechaActualizacion(LocalDateTime.now());
        product.setCategoria(categoria); // ← Ahora la categoría ya está guardada

        // Guardar el producto en la base de datos
        product = productRepository.save(product);
    }

    @Test
    void testRegistrarEntrada_ShouldCreateMovimientoAndUpdateStock() throws Exception {
        // Given
        Long productoId = product.getId();
        Integer cantidadEntrada = 20;
        String motivo = "Reposición de inventario";
        Integer stockInicial = product.getStock(); // 30

        MovimientoStockRequestDTO request = new MovimientoStockRequestDTO();
        request.setProductoId(productoId);
        request.setCantidad(cantidadEntrada);
        request.setMotivo(motivo);

        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then - Registrar entrada
        mockMvc.perform(post("/api/movimientos/entrada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.producto.id", is(productoId.intValue())))
                .andExpect(jsonPath("$.tipoMovimiento", is("ENTRADA")))
                .andExpect(jsonPath("$.cantidad", is(cantidadEntrada)))
                .andExpect(jsonPath("$.motivo", is(motivo)))
                .andExpect(jsonPath("$.id", notNullValue()));

        // Verificar que el stock del producto se actualizó en la base de datos
        Optional<Product> updatedProduct = productRepository.findById(productoId);
        assertTrue(updatedProduct.isPresent());
        assertEquals(stockInicial + cantidadEntrada, updatedProduct.get().getStock());

        // Verificar que se creó el movimiento en la base de datos
        List<MovimientoStock> movimientos = (List<MovimientoStock>) movimientoStockRepository.findAll();
        assertEquals(1, movimientos.size());
        MovimientoStock movimiento = movimientos.get(0);
        assertEquals(productoId, movimiento.getProducto().getId());
        assertEquals(MovimientoStock.TipoMovimiento.ENTRADA, movimiento.getTipoMovimiento());
        assertEquals(cantidadEntrada, movimiento.getCantidad());
        assertEquals(motivo, movimiento.getMotivo());
    }

    @Test
    void testRegistrarSalida_ShouldCreateMovimientoAndUpdateStock() throws Exception {
        // Given
        Long productoId = product.getId();
        Integer cantidadSalida = 10;
        String motivo = "Venta al cliente";
        Integer stockInicial = product.getStock(); // 30

        MovimientoStockRequestDTO request = new MovimientoStockRequestDTO();
        request.setProductoId(productoId);
        request.setCantidad(cantidadSalida);
        request.setMotivo(motivo);

        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then - Registrar salida
        mockMvc.perform(post("/api/movimientos/salida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.producto.id", is(productoId.intValue())))
                .andExpect(jsonPath("$.tipoMovimiento", is("SALIDA")))
                .andExpect(jsonPath("$.cantidad", is(cantidadSalida)))
                .andExpect(jsonPath("$.motivo", is(motivo)))
                .andExpect(jsonPath("$.id", notNullValue()));

        // Verificar que el stock del producto se actualizó en la base de datos
        Optional<Product> updatedProduct = productRepository.findById(productoId);
        assertTrue(updatedProduct.isPresent());
        assertEquals(stockInicial - cantidadSalida, updatedProduct.get().getStock());

        // Verificar que se creó el movimiento en la base de datos
        List<MovimientoStock> movimientos = (List<MovimientoStock>) movimientoStockRepository.findAll();
        assertEquals(1, movimientos.size());
        MovimientoStock movimiento = movimientos.get(0);
        assertEquals(productoId, movimiento.getProducto().getId());
        assertEquals(MovimientoStock.TipoMovimiento.SALIDA, movimiento.getTipoMovimiento());
        assertEquals(cantidadSalida, movimiento.getCantidad());
        assertEquals(motivo, movimiento.getMotivo());
    }

    @Test
    void testRegistrarSalida_WithInsufficientStock_ShouldReturnBadRequest() throws Exception {
        // Given
        Long productoId = product.getId();
        Integer cantidadExcesiva = 100; // Mayor al stock disponible (30)
        String motivo = "Venta";

        MovimientoStockRequestDTO request = new MovimientoStockRequestDTO();
        request.setProductoId(productoId);
        request.setCantidad(cantidadExcesiva);
        request.setMotivo(motivo);

        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then - Intentar registrar salida con stock insuficiente
        mockMvc.perform(post("/api/movimientos/salida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Stock insuficiente")));

        // Verificar que el stock del producto NO cambió
        Optional<Product> unchangedProduct = productRepository.findById(productoId);
        assertTrue(unchangedProduct.isPresent());
        assertEquals(30, unchangedProduct.get().getStock()); // Stock original

        // Verificar que NO se creó ningún movimiento
        List<MovimientoStock> movimientos = (List<MovimientoStock>) movimientoStockRepository.findAll();
        assertEquals(0, movimientos.size());
    }

    @Test
    void testGetAllMovimientos_ShouldReturnAllMovementsFromDatabase() throws Exception {
        // Given - Crear algunos movimientos en la base de datos
        MovimientoStock movimiento1 = new MovimientoStock();
        movimiento1.setProducto(product);
        movimiento1.setTipoMovimiento(MovimientoStock.TipoMovimiento.ENTRADA);
        movimiento1.setCantidad(15);
        movimiento1.setMotivo("Compra inicial");
        movimientoStockRepository.save(movimiento1);

        MovimientoStock movimiento2 = new MovimientoStock();
        movimiento2.setProducto(product);
        movimiento2.setTipoMovimiento(MovimientoStock.TipoMovimiento.SALIDA);
        movimiento2.setCantidad(5);
        movimiento2.setMotivo("Venta");
        movimientoStockRepository.save(movimiento2);

        // When & Then - Obtener todos los movimientos
        mockMvc.perform(get("/api/movimientos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tipoMovimiento", oneOf("ENTRADA", "SALIDA")))
                .andExpect(jsonPath("$[1].tipoMovimiento", oneOf("ENTRADA", "SALIDA")));

        // Verificar en base de datos
        List<MovimientoStock> allMovimientos = (List<MovimientoStock>) movimientoStockRepository.findAll();
        assertEquals(2, allMovimientos.size());
    }
}