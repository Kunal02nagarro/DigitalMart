package com.nagarro.digitalMart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.digitalMart.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByName(String name);

}
