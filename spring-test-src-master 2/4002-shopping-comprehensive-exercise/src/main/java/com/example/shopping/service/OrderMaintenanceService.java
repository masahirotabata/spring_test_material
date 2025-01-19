package com.example.shopping.service;

import java.util.List;

import com.example.shopping.entity.Order;
import com.example.shopping.input.OrderMaintenanceInput;

public interface OrderMaintenanceService {
    List<Order> findAll();

    Order findById(String id);

    void update(OrderMaintenanceInput input);
}
