package com.nagarro.digitalMart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nagarro.digitalMart.model.Product;
import com.nagarro.digitalMart.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
 
    @Autowired
    private ProductService service;
 
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", service.getAllProducts());
        return "products";
    }
 
    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }
 
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        service.saveProduct(product);
        return "redirect:/products";
    }
}
