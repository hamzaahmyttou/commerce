package com.commerce.commerce.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.commerce.dto.LoginRequest;
import com.commerce.commerce.dto.RegisterRequest;
import com.commerce.commerce.dto.TokenResponse;
import com.commerce.commerce.dto.UserDTO;
import com.commerce.commerce.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(userService.register(req.getEmail(), req.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
        String token = userService.login(
                req.getEmail(),
                req.getPassword()
                );
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
