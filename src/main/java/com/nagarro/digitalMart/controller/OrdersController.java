package com.nagarro.digitalMart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nagarro.digitalMart.model.Order;
import com.nagarro.digitalMart.model.User;
import com.nagarro.digitalMart.repository.OrderRepository;
import com.nagarro.digitalMart.repository.UserRepository;

@Controller
public class OrdersController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    public String listOrders(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        List<Order> orders = orderRepository.findByUser(user);
        model.addAttribute("orders", orders);
        return "orders";
    }
}
