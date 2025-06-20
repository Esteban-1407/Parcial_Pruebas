package org.esteban.springboot.springmvc.app.springbootbackend.services.Categorias;

import org.esteban.springboot.springmvc.app.springbootbackend.entities.Categories;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service

public class CategoriaServiceImpl implements CategoriaService{
    final private CategoryRepository categoryRepository;

    public CategoriaServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categories> findAll() {
        return (List<Categories>) categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categories> findById(long id) {
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional
    public Categories save(Categories categories) {
        return categoryRepository.save(categories);
    }

    @Override
    @Transactional
    public Optional<Categories> deleteById(Long id) {
        Optional<Categories> categories = categoryRepository.findById(id);
        if(categories.isPresent()){
            categoryRepository.deleteById(id);
            return categories;
        }
        return Optional.empty();
    }
}
