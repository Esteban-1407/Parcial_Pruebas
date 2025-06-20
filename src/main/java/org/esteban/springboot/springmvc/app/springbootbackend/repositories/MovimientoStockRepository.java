package org.esteban.springboot.springmvc.app.springbootbackend.repositories;

import org.esteban.springboot.springmvc.app.springbootbackend.entities.MovimientoStock;
import org.springframework.data.repository.CrudRepository;

public interface MovimientoStockRepository extends CrudRepository<MovimientoStock,Long> {
}