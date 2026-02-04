package com.nagarro.digitalMart.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class CartService {

    private final Map<Long, Integer> items = new HashMap<>();

    public void addProduct(Long productId, int quantity) {
        items.merge(productId, quantity, Integer::sum);
    }

    public void removeProduct(Long productId) {
        items.remove(productId);
    }

    public Map<Long, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public void clear() {
        items.clear();
    }

    public int getTotalQuantity() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }
}
