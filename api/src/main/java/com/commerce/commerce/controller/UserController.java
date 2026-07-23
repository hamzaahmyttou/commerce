package com.commerce.commerce.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.commerce.commerce.dto.LoginRequest;
import com.commerce.commerce.dto.RegisterRequest;
import com.commerce.commerce.dto.TokenResponse;
import com.commerce.commerce.dto.UserDTO;
import com.commerce.commerce.service.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(userService.register(req.getName(), req.getEmail(), req.getPassword()));
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
