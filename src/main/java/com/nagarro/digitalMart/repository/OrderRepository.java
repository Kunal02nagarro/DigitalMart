package com.nagarro.digitalMart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.digitalMart.model.Order;
import com.nagarro.digitalMart.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUser(User user);
	List<Order> findByUserId(Long userId);
}
