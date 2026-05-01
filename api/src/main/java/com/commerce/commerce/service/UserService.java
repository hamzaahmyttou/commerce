package com.commerce.commerce.service;

import com.commerce.commerce.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.commerce.commerce.dto.UserDTO;
import com.commerce.commerce.entity.User;
import com.commerce.commerce.exception.EmailAlreadyUsedException;
import com.commerce.commerce.exception.InvalidCredentialsException;
import com.commerce.commerce.exception.UserInactiveException;
import com.commerce.commerce.repository.UserRepository;

@Service
public class UserService {
	
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final JwtService jwtService;

    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.modelMapper = modelMapper;
    }

    public UserDTO register(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException();
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        user.setActive(true);

        userRepository.save(user);
        
        return modelMapper.map(user, UserDTO.class);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.getActive()) {
            throw new UserInactiveException();
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return jwtService.generateToken(user);    }
}
