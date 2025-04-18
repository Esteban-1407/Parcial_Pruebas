package org.esteban.springboot.springmvc.app.springbootbackend.repositories;

import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
