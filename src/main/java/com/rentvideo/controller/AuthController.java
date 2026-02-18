package com.rentvideo.controller;

import com.rentvideo.dto.RegisterRequest;
import com.rentvideo.entity.Role;
import com.rentvideo.entity.User;
import com.rentvideo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {

        User user = new User();
        user.setEmail(req.email);
        user.setPassword(passwordEncoder.encode(req.password));
        user.setFirstName(req.firstName);
        user.setLastName(req.lastName);
        user.setRole(req.role == null ? Role.CUSTOMER : req.role);

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
