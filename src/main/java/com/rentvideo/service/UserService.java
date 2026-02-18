package com.rentvideo.service;

import com.rentvideo.dto.UserDto;
import com.rentvideo.entity.Role;
import com.rentvideo.entity.User;
import com.rentvideo.exception.EmailAlreadyExistsException;
import com.rentvideo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto.UserResponse register(UserDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered: " + request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole() != null ? request.getRole() : Role.CUSTOMER)
                .build();

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    private UserDto.UserResponse toResponse(User user) {
        UserDto.UserResponse response = new UserDto.UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole());
        return response;
    }
}
