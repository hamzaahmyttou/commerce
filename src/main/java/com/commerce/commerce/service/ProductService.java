package com.commerce.commerce.service;

import com.commerce.commerce.dto.ProductDTO;
import com.commerce.commerce.entity.*;
import com.commerce.commerce.repository.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
	
    @Autowired
    private ProductRepository productRepository;
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthService authService;

    @Autowired
    private ModelMapper modelMapper;

    public ProductDTO createProduct(ProductDTO productDTO) {
    	Product product = modelMapper.map(productDTO, Product.class);
        String email = authService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow();
        product.setOwner(user);
        product.setCreatedAt(LocalDateTime.now());
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }
    
    public List<ProductDTO> readAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productsDTO = new ArrayList<>();
        products.forEach(product -> {
        		ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        		productsDTO.add(productDTO);
        });
        return productsDTO;
    }

    public ProductDTO readProductById(Long id) {
        Product product = productRepository
        		.findById(id)
        		.orElseThrow(() -> new RuntimeException("Product not found"));
        return modelMapper.map(product, ProductDTO.class);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existing = modelMapper.map(readProductById(id), Product.class);
        String email = authService.getCurrentUserEmail();

        if (!existing.getOwner().getEmail().equals(email)) {
            throw new AccessDeniedException("Not your product");
        }
        
        existing.setName(productDTO.getName());
        existing.setDescription(productDTO.getDescription());
        existing.setPrice(productDTO.getPrice());
        existing.setStock(productDTO.getStock());
        existing.setCategory(productDTO.getCategory());
        existing.setImageUrl(productDTO.getImageUrl());
        return modelMapper.map(productRepository.save(existing), ProductDTO.class);
    }

    public void deleteProductById(Long id) {
        String email = authService.getCurrentUserEmail();

        if (!productRepository.findById(id).orElseThrow().getOwner().getEmail().equals(email)) {
            throw new AccessDeniedException("Not your product");
        }
        productRepository.deleteById(id);
    }
}
