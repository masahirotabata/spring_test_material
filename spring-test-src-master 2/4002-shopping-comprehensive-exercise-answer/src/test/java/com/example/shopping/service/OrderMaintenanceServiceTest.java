package com.example.shopping.service;


import com.example.shopping.enumeration.PaymentMethod;
import com.example.shopping.input.OrderMaintenanceInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql("OrderMaintenanceServiceTest.sql")
class OrderMaintenanceServiceTest {
    @Autowired
    OrderMaintenanceService orderMaintenanceService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void test_update() {
        OrderMaintenanceInput input = new OrderMaintenanceInput();
        input.setId("o01");
        input.setCustomerName("更新後の名前");
        input.setOrderDateTime(LocalDateTime.of(2023, 1, 2, 3, 4));
        input.setBillingAmount(1000);
        input.setCustomerAddress("更新後の住所");
        input.setCustomerEmailAddress("更新後のemail");
        input.setCustomerPhone("更新後のTEL");
        input.setPaymentMethod(PaymentMethod.CONVENIENCE_STORE);

        orderMaintenanceService.update(input);

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
        OrderMaintenanceInput input = new OrderMaintenanceInput();
        input.setId("o99");
        input.setPaymentMethod(PaymentMethod.CONVENIENCE_STORE);

        assertThatThrownBy(() -> {
            orderMaintenanceService.update(input);
        }).isInstanceOf(OptimisticLockingFailureException.class);
    }
}