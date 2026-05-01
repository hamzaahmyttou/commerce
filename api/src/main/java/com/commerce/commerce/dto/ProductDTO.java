package com.commerce.commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {

    @Positive(message = "ID must be greater than zero")
    private Long id;

    @NotBlank(message = "Product name must not be blank")
    private String name;

    private String description;

    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @PositiveOrZero(message = "Stock cannot be negative")
    private Integer stock;

    private String category;

    private String imageUrl;
}
