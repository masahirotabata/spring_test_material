package com.example.shopping.controller;

import com.example.shopping.entity.Order;
import com.example.shopping.enumeration.PaymentMethod;
import com.example.shopping.exception.StockShortageException;
import com.example.shopping.input.OrderInput;
import com.example.shopping.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(OrderSession.class)
class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;


    @Test
    void test_validateInput() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/order/validate-input")
                    .param("name", "東京太郎")
                    .param("address", "東京都")
                    .param("phone", "090-0000-0000")
                    .param("emailAddress", "aaa@example.com")
                    .param("paymentMethod", "BANK")
            )
            .andExpect(view().name("order/orderConfirmation"))
            .andReturn();
        ;

        OrderSession orderSession = (OrderSession)mvcResult.getRequest().getSession().getAttribute("scopedTarget.orderSession");
        OrderInput orderInput = orderSession.getOrderInput();
        assertThat(orderInput.getName()).isEqualTo("東京太郎");
        assertThat(orderInput.getAddress()).isEqualTo("東京都");
        assertThat(orderInput.getPhone()).isEqualTo("090-0000-0000");
        assertThat(orderInput.getEmailAddress()).isEqualTo("aaa@example.com");
        assertThat(orderInput.getPaymentMethod()).isEqualTo(PaymentMethod.BANK);
    }

    @Test
    void test_order() throws Exception {
        Order order = new Order();
        order.setId("o01");
        doReturn(order).when(orderService).placeOrder(any(), any());

        OrderInput orderInput = new OrderInput();
        orderInput.setName("東京太郎");
        OrderSession orderSession = new OrderSession();
        orderSession.setOrderInput(orderInput);

        mockMvc.perform(
                post("/order/place-order")
                    .param("order", "")
                    .sessionAttr("scopedTarget.orderSession", orderSession)
            )
            .andExpect(flash().attribute("order", order))
            .andExpect(redirectedUrl("/order/display-completion"))
        ;

        ArgumentCaptor<OrderInput> captor = ArgumentCaptor.forClass(OrderInput.class);
        verify(orderService).placeOrder(captor.capture(), any());
        OrderInput captored = captor.getValue();

        assertThat(captored).isEqualTo(orderInput);

    }


}