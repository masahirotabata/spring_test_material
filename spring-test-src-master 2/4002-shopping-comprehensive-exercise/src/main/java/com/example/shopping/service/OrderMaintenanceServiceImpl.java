package com.example.shopping.service;

import java.util.List;

import com.example.shopping.input.OrderMaintenanceInput;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping.repository.OrderRepository;
import com.example.shopping.entity.Order;

@Service
@Transactional
public class OrderMaintenanceServiceImpl implements OrderMaintenanceService {
    private final OrderRepository orderRepository;

    public OrderMaintenanceServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.selectAll();
    }

    @Override
    public Order findById(String id) {
        return orderRepository.selectById(id);
    }

    @Override
    public void update(OrderMaintenanceInput input) {
        Order order = new Order();
        order.setId(input.getId());
        order.setOrderDateTime(input.getOrderDateTime());
        order.setBillingAmount(input.getBillingAmount());
        order.setCustomerName(input.getCustomerName());
        order.setCustomerAddress(input.getCustomerAddress());
        order.setCustomerPhone(input.getCustomerPhone());
        order.setCustomerEmailAddress(input.getCustomerEmailAddress());
        order.setPaymentMethod(input.getPaymentMethod());
        boolean result = orderRepository.update(order);
        if (!result) {
            throw new OptimisticLockingFailureException("すでに削除された可能性がある id=" + order.getId());
        }
    }
}
