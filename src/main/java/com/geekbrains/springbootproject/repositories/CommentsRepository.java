package com.geekbrains.springbootproject.repositories;

import com.geekbrains.springbootproject.entities.Category;
import com.geekbrains.springbootproject.entities.Comment;
import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends PagingAndSortingRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    Page<Comment> findAllByProduct(Pageable pageable,Product product);
    Comment findOneByProductAndUser(Product product, User user);
}
