package com.commerce.commerce.controller;

import com.commerce.commerce.config.SecurityConfig;
import com.commerce.commerce.dto.ProductDTO;
import com.commerce.commerce.filter.JwtAuthenticationFilter;
import com.commerce.commerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ProductController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthenticationFilter.class
                )
        }
)
@AutoConfigureMockMvc
class ProductValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldReturn400WhenInvalidProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "id": 0,
                                "name": "",
                                "description": "",
                                "price": 10,
                                "stock": 10
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200WhenValidProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "id": 10,
                                "name": "test",
                                "description": "",
                                "price": 10,
                                "stock": 10,
                                "category": "",
                                "imageUrl": ""
                            }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "id":1,
                                "name":"",
                                "price":10,
                                "stock":5
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenNameIsMissing() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "id":1,
                                "price":10,
                                "stock":5
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPriceIsNegative() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "id":1,
                                "name":"Laptop",
                                "price":-10,
                                "stock":5
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPriceIsZero() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "id":1,
                                "name":"Laptop",
                                "price":0,
                                "stock":5
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenStockIsNegative() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "id":1,
                                "name":"Laptop",
                                "price":10,
                                "stock":-1
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAcceptZeroStock() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "id":1,
                                "name":"Laptop",
                                "price":10,
                                "stock":0
                            }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenIdIsZero() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "id":0,
                                "name":"Laptop",
                                "price":10,
                                "stock":5
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenJsonIsMalformed() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200WhenGetAllProducts() throws Exception {

        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("Laptop");
        dto.setPrice(BigDecimal.TEN);
        dto.setStock(5);

        when(productService.readAllProducts()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn200WhenGetProductById() throws Exception {

        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("Laptop");
        dto.setPrice(BigDecimal.TEN);
        dto.setStock(5);

        when(productService.readProductById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn204WhenDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenUpdateHasInvalidData() throws Exception {
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "id":1,
                                "name":"",
                                "price":-5,
                                "stock":-1
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200WhenUpdateHasValidData() throws Exception {

        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("Updated");
        dto.setPrice(BigDecimal.TEN);
        dto.setStock(2);

        when(productService.updateProduct(org.mockito.ArgumentMatchers.eq(1L),
                org.mockito.ArgumentMatchers.any(ProductDTO.class)))
                .thenReturn(dto);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "id":1,
                        "name":"Updated",
                        "price":10,
                        "stock":2
                    }
                    """))
                .andExpect(status().isOk());
    }
}
