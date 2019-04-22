package com.geekbrains.springbootproject.controllers;

import com.geekbrains.springbootproject.entities.Category;
import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.repositories.specifications.ProductSpecs;
import com.geekbrains.springbootproject.services.CategoriesService;
import com.geekbrains.springbootproject.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoriesController {
    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 5;

    private CategoriesService categoriesService;

    @Autowired
    public void setProductsService(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public String categoriesPage(Model model) {

        ArrayList<Category> categories = (ArrayList<Category>) categoriesService.getAllCategories();

        model.addAttribute("categories", categories);

        return "categories";
    }

    @GetMapping("/{id}")
    public String showHierarchy(Model model, @PathVariable("id") Long id){
        Category currCategory = categoriesService.findById(id);

        ArrayList<Category> hrList = new ArrayList<>();
        hrList = (ArrayList<Category>) currCategory.getHierarchy(hrList);

        model.addAttribute("hierarchy", hrList);

        return "hr-page";

    }

}
