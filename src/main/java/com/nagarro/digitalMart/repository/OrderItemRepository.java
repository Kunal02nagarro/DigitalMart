package com.nagarro.digitalMart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.digitalMart.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
