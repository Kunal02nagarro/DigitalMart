package com.nagarro.digitalMart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nagarro.digitalMart.model.Order;
import com.nagarro.digitalMart.model.OrderItem;
import com.nagarro.digitalMart.model.Product;
import com.nagarro.digitalMart.model.User;
import com.nagarro.digitalMart.repository.OrderRepository;
import com.nagarro.digitalMart.repository.ProductRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order placeOrder(User user, Map<Long, Integer> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;

        for (Map.Entry<Long, Integer> e : cartItems.entrySet()) {
            Long productId = e.getKey();
            int qty = e.getValue();

            Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
            if (product.getStock() < qty) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }

            OrderItem oi = new OrderItem();
            oi.setProduct(product);
            oi.setQuantity(qty);
            items.add(oi);

            // reduce stock
            product.setStock(product.getStock() - qty);
            productRepository.save(product);

            total += product.getPrice() * qty;
        }

        order.setItems(items);
        order.setTotal(total);

        return orderRepository.save(order);
    }
}
