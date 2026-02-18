package com.rentvideo.service;

import com.rentvideo.dto.UserDto;
import com.rentvideo.entity.Role;
import com.rentvideo.entity.User;
import com.rentvideo.exception.EmailAlreadyExistsException;
import com.rentvideo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDto.RegisterRequest request;

    @BeforeEach
    void setUp() {
        request = new UserDto.RegisterRequest();
        request.setEmail("john@example.com");
        request.setPassword("secret123");
        request.setFirstName("John");
        request.setLastName("Doe");
    }

    @Test
    void register_shouldCreateCustomerByDefault() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserDto.UserResponse response = userService.register(request);

        assertEquals(Role.CUSTOMER, response.getRole());
        assertEquals("john@example.com", response.getEmail());
        verify(passwordEncoder).encode("secret123");
    }

    @Test
    void register_shouldAssignAdminRole_whenSpecified() {
        request.setRole(Role.ADMIN);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(2L);
            return u;
        });

        UserDto.UserResponse response = userService.register(request);

        assertEquals(Role.ADMIN, response.getRole());
    }

    @Test
    void register_shouldThrow_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(request));
        verify(userRepository, never()).save(any());
    }
}
