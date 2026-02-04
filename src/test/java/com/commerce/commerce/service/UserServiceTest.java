package com.commerce.commerce.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import com.commerce.commerce.entity.User;
import com.commerce.commerce.exception.EmailAlreadyUsedException;
import com.commerce.commerce.exception.InvalidCredentialsException;
import com.commerce.commerce.exception.UserInactiveException;
import com.commerce.commerce.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void cannotRegisterWithExistingEmail() {
        when(userRepository.existsByEmail("a@test.com")).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class, () -> {
            userService.register("a@test.com", "password");
        });
    }

    @Test
    void cannotLoginWithWrongEmail() {
        when(userRepository.findByEmail("wrong@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.login("wrong@test.com", "HACHED");
        });
    }

    @Test
    void cannotLoginWithWrongPassword() {
        User user = new User();
        user.setEmail("a@test.com");
        user.setPassword("HASHED");
        user.setActive(true);

        when(userRepository.findByEmail("a@test.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "HASHED"))
                .thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.login("a@test.com", "wrong");
        });
    }

    @Test
    void cannotLoginIfUserInactive() {
        User user = new User();
        user.setEmail("a@test.com");
        user.setPassword("HASHED");
        user.setActive(false);

        when(userRepository.findByEmail("a@test.com"))
                .thenReturn(Optional.of(user));

        assertThrows(UserInactiveException.class, () -> {
            userService.login("a@test.com", "password");
        });
    }
}
