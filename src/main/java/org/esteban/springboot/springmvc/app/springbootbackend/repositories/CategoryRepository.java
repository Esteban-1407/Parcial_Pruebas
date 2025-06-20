package org.esteban.springboot.springmvc.app.springbootbackend.repositories;

import org.esteban.springboot.springmvc.app.springbootbackend.entities.Categories;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Categories,Long> {
}
