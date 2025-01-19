package com.example.shopping.controller;

import com.example.shopping.input.CartInput;
import com.example.shopping.input.CartItemInput;
import com.example.shopping.input.OrderInput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shopping.entity.Order;
import com.example.shopping.enumeration.PaymentMethod;
import com.example.shopping.exception.StockShortageException;
import com.example.shopping.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderSession orderSession;

    public OrderController(OrderService orderService, OrderSession orderSession) {
        this.orderService = orderService;
        this.orderSession = orderSession;
    }

    @GetMapping("/display-form")
    public String displayForm(Model model) {
        OrderInput orderInput = new OrderInput();
        orderInput.setPaymentMethod(PaymentMethod.BANK);
        model.addAttribute("orderInput", orderInput);
        return "order/orderForm";
    }

    @PostMapping("/validate-input")
    public String validateInput(
        @Validated OrderInput orderInput, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "order/orderForm";
        }
        orderSession.setOrderInput(orderInput);
        model.addAttribute("cartInput", dummyCartInput());
        return "order/orderConfirmation";
    }

    @PostMapping(value = "/place-order", params = "correct")
    public String correctInput(Model model) {
        model.addAttribute("orderInput", orderSession.getOrderInput());
        return "order/orderForm";
    }

    @PostMapping(value = "/place-order", params = "order")
    public String order(RedirectAttributes redirectAttributes) {
        Order order = orderService
            .placeOrder(orderSession.getOrderInput(), dummyCartInput());
        redirectAttributes.addFlashAttribute("order", order);
        orderSession.clearData();
        return "redirect:/order/display-completion";
    }

    @GetMapping("/display-completion")
    public String displayComplete() {
        return "order/orderCompletion";
    }

    @ExceptionHandler(StockShortageException.class)
    public String displayStockShortagePage() {
        return "order/stockShortage";
    }

    // ハンズオンの都合上、ダミーのカートの中身を用意します。
    private CartInput dummyCartInput() {
        List<CartItemInput> cartItemInputs = new ArrayList<>();

        CartItemInput keshigom = new CartItemInput();
        keshigom.setProductId("p01");
        keshigom.setProductName("消しゴム");
        keshigom.setProductPrice(100);
        keshigom.setQuantity(4);
        cartItemInputs.add(keshigom);

        CartItemInput note = new CartItemInput();
        note.setProductId("p02");
        note.setProductName("ノート");
        note.setProductPrice(200);
        note.setQuantity(5);
        cartItemInputs.add(note);

        CartInput cartInput = new CartInput();
        cartInput.setCartItemInputs(cartItemInputs);
        return cartInput;
    }

}
