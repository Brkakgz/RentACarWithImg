package com.rentacar6.rentacar6.controller;

import com.rentacar6.rentacar6.model.Admin;
import com.rentacar6.rentacar6.model.Customer;
import com.rentacar6.rentacar6.repository.AdminRepository;
import com.rentacar6.rentacar6.repository.CustomerRepository;
import com.rentacar6.rentacar6.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * POST /login endpoint for user and admin authentication
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        try {
            // Customer authentication
            Optional<Customer> customer = customerRepository.findByEmail(email);
            if (customer.isPresent() && passwordEncoder.matches(password, customer.get().getPassword())) {
                String token = jwtService.generateToken(email, "ROLE_USER");
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("role", "USER");
                return ResponseEntity.ok(response);
            }

            // Admin authentication
            Optional<Admin> admin = adminRepository.findByEmail(email);
            if (admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword())) {
                String token = jwtService.generateToken(email, "ROLE_ADMIN");
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("role", "ADMIN");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            logger.error("Login failed for email {}: {}", email, e.getMessage());
        }


        // Invalid credentials
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    /**
     * GET /login endpoint to handle browser GET requests
     */
    @GetMapping("/login")
    public ResponseEntity<String> loginPage() {
        return ResponseEntity.ok("Login endpoint is POST-only. Please use POST method with credentials.");
    }
}
