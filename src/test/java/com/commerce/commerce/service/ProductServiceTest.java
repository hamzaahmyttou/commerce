package com.commerce.commerce.service;

import com.commerce.commerce.dto.ProductDTO;
import com.commerce.commerce.entity.Product;
import com.commerce.commerce.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;

import com.commerce.commerce.repository.ProductRepository;
import com.commerce.commerce.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    AuthService authService;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    ProductService productService;

    User owner;
    User anotherUser;
    Product product;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setEmail("owner@owner.com");
        userRepository.save(owner);

        anotherUser = new User();
        anotherUser.setEmail("other@other.com");
        userRepository.save(anotherUser);

        product = new Product();
        product.setId(100L);
        product.setOwner(owner);
        productRepository.save(product);
    }
    
    @Test
    void cannotUpdateProductOfAnotherUser() {
        when(authService.getCurrentUserEmail()).thenReturn("other@other.com");
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        assertThrows(AccessDeniedException.class, () -> {
            productService.updateProduct(product.getId(), new ProductDTO());
        });
    }
    
    @Test
    void cannotDeleteProductOfAnotherUser() {
        when(authService.getCurrentUserEmail()).thenReturn("other@other.com");
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        assertThrows(AccessDeniedException.class, () -> {
            productService.deleteProductById(product.getId());
        });
    }

    @Test
    void ownerCanUpdateProduct() {
        when(authService.getCurrentUserEmail()).thenReturn("owner@owner.com");
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        ProductDTO productDTO = new ProductDTO();

        when(productRepository.save(product)).thenReturn(product);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        productDTO.setName("testNm");
        productDTO.setDescription("testDes");
        productDTO.setPrice(BigDecimal.valueOf(11.11));
        productDTO.setStock(11);
        productDTO.setCategory("testCat");
        productDTO.setImageUrl("testUrl");

        productService.updateProduct(product.getId(), productDTO);

        assertEquals("testNm", product.getName());
        assertEquals("testDes", product.getDescription());
        assertEquals(BigDecimal.valueOf(11.11), product.getPrice());
        assertEquals(11, product.getStock());
        assertEquals("testCat", product.getCategory());
        assertEquals("testUrl", product.getImageUrl());
    }

    @Test
    void ownerCanDeleteProduct() {
        when(authService.getCurrentUserEmail()).thenReturn("owner@owner.com");
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        productService.deleteProductById(product.getId());

        verify(productRepository).deleteById(product.getId());
    }
}
