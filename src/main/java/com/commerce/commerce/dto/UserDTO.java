package com.commerce.commerce.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Boolean active;
}
