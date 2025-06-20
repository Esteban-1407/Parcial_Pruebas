package org.esteban.springboot.springmvc.app.springbootbackend.services.Product;

import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product>findAll();
    Optional<Product> findById(long id);
    Product save(Product product);
    Optional<Product>deleteById(Long id);
}
