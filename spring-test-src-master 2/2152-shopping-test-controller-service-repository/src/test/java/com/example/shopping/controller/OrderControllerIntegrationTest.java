package com.example.shopping.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("OrderControllerIntegrationTest.sql")
class OrderControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void test_order() throws Exception {
        mockMvc.perform(
                post("/order/place-order")
                    .param("order", "")
                    .param("name", "東京太郎")
                    .param("address", "東京都")
                    .param("phone", "090-0000-0000")
                    .param("emailAddress", "taro@example.com")
                    .param("paymentMethod", "BANK")
            )
            .andExpect(view().name("order/orderCompletion"))
        ;

    }
}