package org.esteban.springboot.springmvc.app.springbootbackend.services.Categorias;

import org.esteban.springboot.springmvc.app.springbootbackend.entities.Categories;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service

public interface CategoriaService {
    List<Categories> findAll();
    Optional<Categories> findById(long id);
    Categories save(Categories categoria);
    Optional<Categories>deleteById(Long id);
}
