package com.example.shopping.repository;

import java.util.List;

import com.example.shopping.entity.Order;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean update(Order order) {
        int count = jdbcTemplate.update("update t_order set order_date_time=?, billing_amount=?, customer_name=?, customer_address=?, customer_phone=?, customer_email_address=?, payment_method=? where id=?",
            order.getOrderDateTime(),
            order.getBillingAmount(),
            order.getCustomerName(),
            order.getCustomerAddress(),
            order.getCustomerPhone(),
            order.getCustomerEmailAddress(),
            order.getPaymentMethod().toString(),
            order.getId());
        return count > 0;
    }

    @Override
    public Order selectById(String id) {
        try {
            return jdbcTemplate.queryForObject("select * from t_order where id=?", new DataClassRowMapper<>(Order.class), id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Order> selectAll() {
        return jdbcTemplate.query("select * from t_order", new DataClassRowMapper<>(Order.class));
    }
}
