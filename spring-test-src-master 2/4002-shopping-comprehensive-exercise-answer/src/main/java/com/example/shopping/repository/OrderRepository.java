package com.example.shopping.repository;

import java.util.List;

import com.example.shopping.entity.Order;

public interface OrderRepository {
    boolean update(Order order);

    Order selectById(String id);

    List<Order> selectAll();

}
