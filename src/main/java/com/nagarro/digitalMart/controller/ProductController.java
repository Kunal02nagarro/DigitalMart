package com.nagarro.digitalMart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nagarro.digitalMart.model.Product;
import com.nagarro.digitalMart.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
 
    @Autowired
    private ProductService service;
 
    // View all products 
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", service.getAllProducts());
        return "products";
    }
 
    // Show add product form
    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }
 
    // Add new product 
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addProduct(@ModelAttribute Product product) {
        service.saveProduct(product);
        return "redirect:/products";
    }
    
    // Admin endpoint for add product
    @GetMapping("/admin/add-product")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }
    
    // Admin endpoint for saving product
    @PostMapping("/admin/add-product")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAddProduct(@ModelAttribute Product product) {
        service.saveProduct(product);
        return "redirect:/products";
    }
    
    // Get product details by ID 
    @GetMapping("/{id}")
    public String viewProductDetail(@PathVariable Long id, Model model) {
        Product product = service.findById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "product-detail";
    }
}


