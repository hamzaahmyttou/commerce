package com.commerce.commerce.service;

import com.commerce.commerce.dto.ProductDTO;
import com.commerce.commerce.entity.*;
import com.commerce.commerce.repository.*;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.procedure.ParameterMisuseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<Product> existing = productRepository.findById(id);
        if (existing.isEmpty()) {
            throw new EntityNotFoundException("Product not found");
        } else {
            String email = authService.getCurrentUserEmail();

            if (!existing.get().getOwner().getEmail().equals(email)) {
                throw new AccessDeniedException("Not your product");
            }

            existing.get().setName(productDTO.getName());
            existing.get().setDescription(productDTO.getDescription());
            existing.get().setPrice(productDTO.getPrice());
            existing.get().setStock(productDTO.getStock());
            existing.get().setCategory(productDTO.getCategory());
            existing.get().setImageUrl(productDTO.getImageUrl());
            return modelMapper.map(productRepository.save(existing.get()), ProductDTO.class);
        }
    }

    public void deleteProductById(Long id) {
        String email = authService.getCurrentUserEmail();

        if (!productRepository.findById(id).orElseThrow().getOwner().getEmail().equals(email)) {
            throw new AccessDeniedException("Not your product");
        }
        productRepository.deleteById(id);
    }
}
