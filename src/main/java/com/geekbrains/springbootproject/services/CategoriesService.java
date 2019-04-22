package com.geekbrains.springbootproject.services;

import com.geekbrains.springbootproject.entities.Category;
import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.repositories.CategoriesRepository;
import com.geekbrains.springbootproject.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {
    private CategoriesRepository categoriesRepository;

    @Autowired
    public void setProductsRepository(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public Category findByTitle(String title) {
        return categoriesRepository.findOneByTitle(title);
    }

    public Category findById(Long id) {
        return categoriesRepository.findById(id).orElse(null);
    }

    public List<Category> getAllCategories() {
        return (List<Category>) categoriesRepository.findAll();
    }

    public Category saveOrUpdate(Category category) {
        return categoriesRepository.save(category);
    }

    public List<Category> getRootCategories(){return categoriesRepository.findAllByLevel(0L);}

    public List<Category> getChildren(Category category){return categoriesRepository.findAllByParent(category);}

}
