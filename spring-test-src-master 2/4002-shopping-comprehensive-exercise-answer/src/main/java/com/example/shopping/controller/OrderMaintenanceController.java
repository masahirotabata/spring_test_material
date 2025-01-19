package com.example.shopping.controller;

import java.util.List;

import com.example.shopping.input.OrderMaintenanceInput;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shopping.entity.Order;
import com.example.shopping.service.OrderMaintenanceService;

@Controller
@RequestMapping("/maintenance/order")
public class OrderMaintenanceController {
    private static final String BASE_VIEW_NAME = "maintenance/order/";
    private final OrderMaintenanceService orderMaintenanceService;

    public OrderMaintenanceController(
        OrderMaintenanceService orderMaintenanceService
    ) {
        this.orderMaintenanceService = orderMaintenanceService;
    }

    @GetMapping("/display-list")
    public String displayList(Model model) {
        List<Order> orders = orderMaintenanceService.findAll();
        model.addAttribute(orders);
        return BASE_VIEW_NAME + "orderList";
    }

    @GetMapping("/display-update-form")
    public String displayUpdateForm(@RequestParam String id, Model model) {
        Order order = orderMaintenanceService.findById(id);
        OrderMaintenanceInput orderMaintenanceInput = new OrderMaintenanceInput();
        orderMaintenanceInput.setId(order.getId());
        orderMaintenanceInput.setOrderDateTime(order.getOrderDateTime());
        orderMaintenanceInput.setBillingAmount(order.getBillingAmount());
        orderMaintenanceInput.setCustomerName(order.getCustomerName());
        orderMaintenanceInput.setCustomerAddress(order.getCustomerAddress());
        orderMaintenanceInput.setCustomerPhone(order.getCustomerPhone());
        orderMaintenanceInput.setCustomerEmailAddress(order.getCustomerEmailAddress());
        orderMaintenanceInput.setPaymentMethod(order.getPaymentMethod());
        model.addAttribute(orderMaintenanceInput);
        return BASE_VIEW_NAME + "updateForm";
    }

    @PostMapping(value = "/validate-update-input")
    public String validateUpdateForm(@Validated OrderMaintenanceInput form, BindingResult bindResult) {
        if (bindResult.hasErrors()) {
            return BASE_VIEW_NAME + "updateForm";
        }
        return BASE_VIEW_NAME + "updateConfirmation";
    }

    @PostMapping(value = "/update", params = "correct")
    public String correctUpdateForm(@Validated OrderMaintenanceInput form) {
        return BASE_VIEW_NAME + "updateForm";
    }

    @PostMapping(value = "/update", params = "update")
    public String doUpdate(@Validated OrderMaintenanceInput input, Model model) {
        orderMaintenanceService.update(input);
        model.addAttribute("orderId", input.getId());
        return BASE_VIEW_NAME + "updateCompletion";
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public String displayUpdateFailure() {
        return BASE_VIEW_NAME + "updateFailure";
    }
}
