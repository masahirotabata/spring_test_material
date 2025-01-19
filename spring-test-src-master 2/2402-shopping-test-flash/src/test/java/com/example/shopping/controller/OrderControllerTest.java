package com.example.shopping.controller;

import com.example.shopping.entity.Order;
import com.example.shopping.exception.StockShortageException;
import com.example.shopping.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;


    @Test
    void test_validateInput() throws Exception {
        mockMvc.perform(
                post("/order/validate-input")
            )
            .andExpect(view().name("order/orderForm"))
            .andExpect(model().attributeHasFieldErrors("orderInput",
                "name", "address", "phone", "emailAddress", "paymentMethod"))
        ;
    }

    @Test
    void test_order() throws Exception {
        Order order = new Order();
        order.setId("o01");
        doReturn(order).when(orderService).placeOrder(any(), any());

        mockMvc.perform(
                post("/order/place-order")
                    .param("order", "")
                    .param("name", "東京太郎")
                    .param("address", "東京都")
                    .param("phone", "090-0000-0000")
                    .param("emailAddress", "aaa@example.com")
                    .param("paymentMethod", "BANK")
            )
        ;
    }

    @Test
    void test_order_在庫不足() throws Exception {
        doThrow(StockShortageException.class).when(orderService).placeOrder(any(), any());

        mockMvc.perform(
                post("/order/place-order")
                    .param("order", "")
                    .param("name", "東京太郎")
                    .param("address", "東京都")
                    .param("phone", "090-0000-0000")
                    .param("emailAddress", "aaa@example.com")
                    .param("paymentMethod", "BANK")
            )
            .andExpect(content().string(containsString("在庫不足")))
        ;
    }

}