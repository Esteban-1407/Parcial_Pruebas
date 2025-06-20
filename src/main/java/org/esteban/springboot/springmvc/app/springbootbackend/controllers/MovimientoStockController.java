package org.esteban.springboot.springmvc.app.springbootbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.esteban.springboot.springmvc.app.springbootbackend.dto.MovimientoStockRequestDTO;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.MovimientoStock;
import org.esteban.springboot.springmvc.app.springbootbackend.services.MovimientoStock.MovimientoStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MovimientoStockController {

    private final MovimientoStockService movimientoStockService;

    // GET /api/movimientos - Obtener todos los movimientos
    @GetMapping
    public ResponseEntity<List<MovimientoStock>> getAllMovimientos() {
        List<MovimientoStock> movimientos = movimientoStockService.findAll();
        return ResponseEntity.ok(movimientos);
    }

    // GET /api/movimientos/{id} - Obtener movimiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoStock> getMovimientoById(@PathVariable Long id) {
        return movimientoStockService.findById(id)
                .map(movimiento -> ResponseEntity.ok(movimiento))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/movimientos/entrada - Registrar entrada de stock
    @PostMapping("/entrada")
    public ResponseEntity<?> registrarEntrada(@Validated @RequestBody MovimientoStockRequestDTO request) {
        try {
            MovimientoStock movimiento = movimientoStockService.registrarEntrada(
                    request.getProductoId(),
                    request.getCantidad(),
                    request.getMotivo()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/movimientos/salida - Registrar salida de stock
    @PostMapping("/salida")
    public ResponseEntity<?> registrarSalida(@Validated @RequestBody MovimientoStockRequestDTO request) {
        try {
            MovimientoStock movimiento = movimientoStockService.registrarSalida(
                    request.getProductoId(),
                    request.getCantidad(),
                    request.getMotivo()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/movimientos - Crear movimiento genérico (para casos especiales)
    @PostMapping
    public ResponseEntity<MovimientoStock> createMovimiento(@Validated @RequestBody MovimientoStock movimiento) {
        // Limpiar ID para asegurar que es un nuevo movimiento
        movimiento.setId(null);
        MovimientoStock savedMovimiento = movimientoStockService.save(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovimiento);
    }

    // DELETE /api/movimientos/{id} - Eliminar movimiento (solo para casos excepcionales)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimiento(@PathVariable Long id) {
        if (movimientoStockService.findById(id).isPresent()) {
            movimientoStockService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // POST /api/movimientos/actualizar-stock - Endpoint para actualizar stock (para el frontend)
    @PostMapping("/actualizar-stock")
    public ResponseEntity<?> actualizarStock(@RequestBody Map<String, Object> request) {
        try {
            Long productoId = Long.valueOf(request.get("productoId").toString());
            Integer cantidad = Integer.valueOf(request.get("cantidad").toString());
            String tipoMovimiento = request.get("tipoMovimiento").toString();
            String motivo = request.get("motivo") != null ? request.get("motivo").toString() : "";

            MovimientoStock movimiento;

            if ("ENTRADA".equals(tipoMovimiento)) {
                movimiento = movimientoStockService.registrarEntrada(productoId, cantidad, motivo);
            } else if ("SALIDA".equals(tipoMovimiento)) {
                movimiento = movimientoStockService.registrarSalida(productoId, cantidad, motivo);
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Tipo de movimiento inválido. Use ENTRADA o SALIDA"));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Stock actualizado correctamente",
                    "movimiento", movimiento
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}