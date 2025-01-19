package com.example.shopping.controller;


import com.example.shopping.entity.Order;
import com.example.shopping.service.OrderMaintenanceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderMaintenanceController.class)
class OrderMaintenanceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderMaintenanceService orderMaintenanceService;

    @Test
    void test_displayUpdateForm() throws Exception {
        Order order = new Order();
        order.setCustomerName("東京太郎");
        Mockito.doReturn(order).when(orderMaintenanceService).findById("o01");

        mockMvc.perform(
                get("/maintenance/order/display-update-form").param("id", "o01")
            )
            .andExpect(status().isOk())
            .andExpect(view().name("maintenance/order/updateForm"))
            .andExpect(content().string(containsString("東京太郎")))
        ;
    }

    @Test
    void test_validateUpdateForm() throws Exception {
        mockMvc.perform(
                post("/maintenance/order/validate-update-input")
            )
            .andExpect(view().name("maintenance/order/updateForm"))
            .andExpect(model().attributeHasFieldErrors("orderMaintenanceInput",
                "customerName", "customerAddress", "customerPhone", "customerEmailAddress", "paymentMethod"))
        ;

    }

}