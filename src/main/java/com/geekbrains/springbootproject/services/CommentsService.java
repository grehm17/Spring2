package com.geekbrains.springbootproject.services;

import com.geekbrains.springbootproject.entities.Category;
import com.geekbrains.springbootproject.entities.Comment;
import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.entities.User;
import com.geekbrains.springbootproject.repositories.CategoriesRepository;
import com.geekbrains.springbootproject.repositories.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService {
    private CommentsRepository commentsRepository;

    @Autowired
    public void setProductsRepository(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public Comment findByProductAndUser(Product product, User user) {
        return commentsRepository.findOneByProductAndUser(product,user);
    }

    public Page<Comment> findAllByProduct(Product product, Integer pgNumber) {
        return commentsRepository.findAllByProduct(PageRequest.of(pgNumber, 5),product);
    }

    public Comment saveOrUpdate(Comment comment) {
        return commentsRepository.save(comment);
    }

}
