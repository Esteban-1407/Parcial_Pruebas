package org.esteban.springboot.springmvc.app.springbootbackend.services.MovimientoStock;

import org.esteban.springboot.springmvc.app.springbootbackend.entities.MovimientoStock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface MovimientoStockService {
    List<MovimientoStock> findAll();
    Optional<MovimientoStock> findById(Long id);
    MovimientoStock save(MovimientoStock movimientoStock);
    void deleteById(Long id);

    MovimientoStock registrarEntrada(Long productoId, Integer cantidad, String motivo);
    MovimientoStock registrarSalida(Long productoId, Integer cantidad, String motivo);
}

