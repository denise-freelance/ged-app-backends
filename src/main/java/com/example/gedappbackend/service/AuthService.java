package com.example.gedappbackend.service;

import com.example.gedappbackend.model.User;
import com.example.gedappbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuditService auditService;

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

            // Update last login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            String token = jwtTokenProvider.generateToken(authentication);

            auditService.log(user.getId(), "LOGIN", "USER", null, null);
            log.info("User logged in: {}", request.getUsername());

            return AuthResponse.builder()
                    .token(token)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();

        } catch (Exception e) {
            log.error("Login failed for user: {}", request.getUsername(), e);
            throw new AuthenticationException("Invalid username or password");
        }
    }

    public AuthResponse register(RegisterRequest request) {
        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }

        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.READER)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        String token = jwtTokenProvider.generateTokenFromUserId(user.getId(), user.getUsername(), user.getRole());

        auditService.log(user.getId(), "REGISTER", "USER", user.getId(), null);
        log.info("New user registered: {}", request.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public void logout(UUID userId) {
        auditService.log(userId, "LOGOUT", "USER", null, null);
        log.info("User logged out: {}", userId);
    }
}
