package com.nagarro.digitalMart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.digitalMart.model.Product;
import com.nagarro.digitalMart.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	public List<Product> findAll() {
		return productRepository.findAll();
	}
	
	public Product findById(Long id) {
		Optional<Product> product = productRepository.findById(id);
		return product.orElse(null);
	}
	
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}
	
	public Product save(Product product) {
		return productRepository.save(product);
	}
	
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
	
	public void delete(Long id) {
		productRepository.deleteById(id);
	}

}
