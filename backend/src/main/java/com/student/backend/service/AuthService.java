package com.student.backend.service;

import com.student.backend.dto.LoginRequest;
import com.student.backend.dto.RegisterRequest;
import com.student.backend.dto.AuthResponse;
import com.student.backend.model.Role;
import com.student.backend.model.User;
import com.student.backend.repository.RoleRepository;
import com.student.backend.repository.UserRepository;
import com.student.backend.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getRoles().stream()
                .findFirst()
                .map(r -> r.getName().toString())
                .orElse("ROLE_STUDENT");

        String token = tokenProvider.generateToken(user.getUsername(), role);

        return new AuthResponse(token, "Bearer", user.getId(), user.getUsername(), user.getEmail(), role);
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());

        Set<Role> roles = new HashSet<>();
        String roleStr = registerRequest.getRole() != null ? registerRequest.getRole() : "ROLE_STUDENT";
        Role role = roleRepository.findByName(Role.ERole.valueOf(roleStr))
                .orElseGet(() -> {
                    Role newRole = new Role(null, Role.ERole.ROLE_STUDENT);
                    return roleRepository.save(newRole);
                });
        roles.add(role);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        String token = tokenProvider.generateToken(savedUser.getUsername(), role.getName().toString());

        return new AuthResponse(token, "Bearer", savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
                role.getName().toString());
    }
}
