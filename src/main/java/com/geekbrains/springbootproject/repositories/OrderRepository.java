package com.geekbrains.springbootproject.repositories;

import com.geekbrains.springbootproject.entities.Order;
import com.geekbrains.springbootproject.entities.OrderItem;
import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    @Query("SELECT  oi FROM  OrderItem oi INNER JOIN oi.order o WHERE o.user = :us AND oi.product =:pr")
    List<OrderItem> findByUserProduct(@Param("pr") Product pr, @Param("us") User us);
}
