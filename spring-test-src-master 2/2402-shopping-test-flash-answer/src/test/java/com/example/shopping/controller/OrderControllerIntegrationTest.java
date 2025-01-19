package com.example.shopping.controller;

import com.example.shopping.entity.Order;
import com.example.shopping.enumeration.PaymentMethod;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("OrderControllerIntegrationTest.sql")
class OrderControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void test_order() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                post("/order/place-order")
                    .param("order", "")
                    .param("name", "東京太郎")
                    .param("address", "東京都")
                    .param("phone", "090-0000-0000")
                    .param("emailAddress", "taro@example.com")
                    .param("paymentMethod", "BANK")
            )
            .andExpect(redirectedUrl("/order/display-completion"))
            .andReturn();

        Order order = (Order) mvcResult.getFlashMap().get("order");
        Map<String, Object> orderMap = jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE id=?", order.getId());
        assertThat(orderMap.get("customer_name")).isEqualTo("東京太郎");
        assertThat(orderMap.get("customer_phone")).isEqualTo("090-0000-0000");
        assertThat(orderMap.get("customer_address")).isEqualTo("東京都");
        assertThat(orderMap.get("customer_email_address")).isEqualTo("taro@example.com");
        assertThat(orderMap.get("payment_method")).isEqualTo(PaymentMethod.BANK.toString());
        assertThat(orderMap.get("order_date_time")).isNotNull();

    }

}