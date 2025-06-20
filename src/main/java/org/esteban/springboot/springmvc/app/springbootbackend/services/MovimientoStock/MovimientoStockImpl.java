package org.esteban.springboot.springmvc.app.springbootbackend.services.MovimientoStock;

import org.esteban.springboot.springmvc.app.springbootbackend.entities.MovimientoStock;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.MovimientoStockRepository;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service

public class MovimientoStockImpl implements MovimientoStockService {
    private final MovimientoStockRepository movimientoStockRepository;
    private final ProductRepository productoRepository;

    public MovimientoStockImpl(MovimientoStockRepository movimientoStockRepository, ProductRepository productoRepository) {
        this.movimientoStockRepository = movimientoStockRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional (readOnly = true)
    public List<MovimientoStock> findAll() {
        return (List<MovimientoStock>) movimientoStockRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovimientoStock> findById(Long id) {
        return movimientoStockRepository.findById(id);
    }

    @Override
    public MovimientoStock save(MovimientoStock movimientoStock) {
        return movimientoStockRepository.save(movimientoStock);
    }

    @Override
    public void deleteById(Long id) {
        movimientoStockRepository.deleteById(id);
    }

    @Override
    public MovimientoStock registrarEntrada(Long productoId, Integer cantidad, String motivo) {
        // Buscar el producto
        Product producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Actualizar stock del producto
        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);

        // Crear y guardar movimiento
        MovimientoStock movimiento = new MovimientoStock();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(MovimientoStock.TipoMovimiento.ENTRADA);
        movimiento.setCantidad(cantidad);
        movimiento.setMotivo(motivo);

        return movimientoStockRepository.save(movimiento);
    }

    @Override
    public MovimientoStock registrarSalida(Long productoId, Integer cantidad, String motivo) {
        // Buscar el producto
        Product producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validar stock suficiente
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        // Actualizar stock del producto
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        // Crear y guardar movimiento
        MovimientoStock movimiento = new MovimientoStock();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(MovimientoStock.TipoMovimiento.SALIDA);
        movimiento.setCantidad(cantidad);
        movimiento.setMotivo(motivo);

        return movimientoStockRepository.save(movimiento);
    }
}
