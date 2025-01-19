package com.example.shopping.repository;

import com.example.shopping.entity.Order;
import com.example.shopping.enumeration.PaymentMethod;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Sql("JdbcOrderRepositoryTest.sql")
class JdbcOrderRepositoryTest {
    JdbcOrderRepository jdbcOrderRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcOrderRepository = new JdbcOrderRepository(jdbcTemplate);
    }

    @Test
    void test_update() {
        Order order = new Order();
        order.setId("o01");
        order.setCustomerName("更新後の名前");
        order.setOrderDateTime(LocalDateTime.of(2023, 1, 2, 3, 4));
        order.setBillingAmount(1000);
        order.setCustomerAddress("更新後の住所");
        order.setCustomerEmailAddress("更新後のemail");
        order.setCustomerPhone("更新後のTEL");
        order.setPaymentMethod(PaymentMethod.CONVENIENCE_STORE);

        boolean result = jdbcOrderRepository.update(order);
        assertThat(result).isEqualTo(true);

        Map<String, Object> updated = jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE id=?", "o01");
        assertThat(updated.get("customer_name")).isEqualTo("更新後の名前");
        assertThat(updated.get("customer_address")).isEqualTo("更新後の住所");
        assertThat(updated.get("customer_phone")).isEqualTo("更新後のTEL");
        assertThat(updated.get("customer_email_address")).isEqualTo("更新後のemail");
        assertThat(updated.get("billing_amount")).isEqualTo(1000);
        assertThat(updated.get("order_date_time")).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2023, 1, 2, 3, 4)));
        assertThat(updated.get("payment_method")).isEqualTo("CONVENIENCE_STORE");
    }

    @Test
    void test_update_fail() {
        Order order = new Order();
        order.setId("o99");
        order.setPaymentMethod(PaymentMethod.CONVENIENCE_STORE);
        boolean result = jdbcOrderRepository.update(order);
        assertThat(result).isEqualTo(false);
    }

    @Test
    void test_selectById() {
        Order order = jdbcOrderRepository.selectById("o01");
        assertThat(order.getCustomerName()).isEqualTo("cname01");
        assertThat(order.getCustomerAddress()).isEqualTo("caddress01");
        assertThat(order.getCustomerPhone()).isEqualTo("090-0000-0001");
        assertThat(order.getCustomerEmailAddress()).isEqualTo("cname01@example.com");
        assertThat(order.getBillingAmount()).isEqualTo(770);
        assertThat(order.getOrderDateTime()).isEqualTo(LocalDateTime.of(2023, 1, 1, 0, 0));
        assertThat(order.getPaymentMethod()).isEqualTo(PaymentMethod.BANK);
    }

    @Test
    void test_selectById_noData() {
        Order order = jdbcOrderRepository.selectById("o99");
        assertThat(order).isNull();
    }

    @Test
    void test_selectAll() {
        List<Order> orders = jdbcOrderRepository.selectAll();
        assertThat(orders.size()).isEqualTo(2);
    }

}