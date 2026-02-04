package com.nagarro.digitalMart.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nagarro.digitalMart.model.Product;
import com.nagarro.digitalMart.model.Order;
import com.nagarro.digitalMart.model.User;
import com.nagarro.digitalMart.service.CartService;
import com.nagarro.digitalMart.service.OrderService;
import com.nagarro.digitalMart.service.ProductService;
import com.nagarro.digitalMart.repository.UserRepository;

@Controller
@RequestMapping
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam(defaultValue = "1") int qty, RedirectAttributes ra) {
        Product p = productService.findById(id);
        if (p == null) {
            ra.addFlashAttribute("error", "Product not found");
            return "redirect:/products";
        }
        if (p.getStock() < qty) {
            ra.addFlashAttribute("error", "Not enough stock");
            return "redirect:/products";
        }
        cartService.addProduct(id, qty);
        ra.addFlashAttribute("success", "Added to cart");
        return "redirect:/products";
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        Map<Long, Integer> items = cartService.getItems();
        List<CartItemView> list = new ArrayList<>();
        double total = 0.0;
        for (Map.Entry<Long, Integer> e : items.entrySet()) {
            Product p = productService.findById(e.getKey());
            if (p == null) continue;
            int qty = e.getValue();
            double subtotal = p.getPrice() * qty;
            total += subtotal;
            list.add(new CartItemView(p, qty, subtotal));
        }
        model.addAttribute("items", list);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, RedirectAttributes ra) {
        cartService.removeProduct(id);
        ra.addFlashAttribute("success", "Removed from cart");
        return "redirect:/cart";
    }

    @PostMapping("/order/place")
    @PreAuthorize("isAuthenticated()")
    public String placeOrder(Principal principal, RedirectAttributes ra) {
        try {
            String username = principal.getName();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
            Order order = orderService.placeOrder(user, cartService.getItems());
            cartService.clear();
            ra.addFlashAttribute("success", "Order placed successfully (Order id: " + order.getId() + ")");
            return "redirect:/orders";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/cart";
        }
    }

    // Simple view model for cart item
    public static class CartItemView {
        private Product product;
        private int quantity;
        private double subtotal;

        public CartItemView(Product product, int quantity, double subtotal) {
            this.product = product;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }

        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public double getSubtotal() { return subtotal; }
    }
}
