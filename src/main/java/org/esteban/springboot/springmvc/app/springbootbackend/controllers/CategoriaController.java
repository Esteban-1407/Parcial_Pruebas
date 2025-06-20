package org.esteban.springboot.springmvc.app.springbootbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.Categories;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.esteban.springboot.springmvc.app.springbootbackend.services.Categorias.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    // GET /api/categorias - Obtener todas las categorías
    @GetMapping
    public ResponseEntity<List<Categories>> getAllCategorias() {
        List<Categories> categorias = categoriaService.findAll();
        return ResponseEntity.ok(categorias);
    }

    // GET /api/categorias/{id} - Obtener categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<Categories> getCategoriaById(@PathVariable Long id) {
        return categoriaService.findById(id)
                .map(categoria -> ResponseEntity.ok(categoria))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/categorias - Crear nueva categoría
    @PostMapping
    public ResponseEntity<Categories> createCategoria(@Validated @RequestBody Categories categoria) {
        // Limpiar ID para asegurar que es una nueva categoría
        categoria.setId(null);
        Categories savedCategoria = categoriaService.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoria);
    }

    // PUT /api/categorias/{id} - Actualizar categoría
    @PutMapping("/{id}")
    public ResponseEntity<Categories> updateCategoria(@PathVariable Long id,
                                                     @Validated @RequestBody Categories categoria) {
        return categoriaService.findById(id)
                .map(existingCategoria -> {
                    categoria.setId(id);
                    // Mantener fechas de auditoría
                    categoria.setFechaCreacion(existingCategoria.getFechaCreacion());
                    Categories updatedCategoria = categoriaService.save(categoria);
                    return ResponseEntity.ok(updatedCategoria);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/categorias/{id} - Eliminar categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        if (categoriaService.findById(id).isPresent()) {
            categoriaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /api/categorias/{id}/productos - Obtener productos de una categoría
    @GetMapping("/{id}/productos")
    public ResponseEntity<List<Product>> getProductosByCategoria(@PathVariable Long id) {
        return categoriaService.findById(id)
                .map(categoria -> ResponseEntity.ok(categoria.getProductos()))
                .orElse(ResponseEntity.notFound().build());
    }}

