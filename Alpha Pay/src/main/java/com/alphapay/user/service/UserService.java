package com.alphapay.user.service;

import com.alphapay.user.entity.User;
import com.alphapay.user.repository.UserRepository;
import com.alphapay.user.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RestTemplate restTemplate;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Wallet ServiceURL
//    private final String WALLET_SERVICE_URL = "http://localhost:8083/alphapay/wallet/create?userId=";
    private final String WALLET_SERVICE_URL = "http://wallet-service:8083/alphapay/wallet/create?userId=";

    public User registerUser(User user) {
        try {
            User existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser != null) {
                throw new RuntimeException("Email already registered!");
            }

            // Encode password and save user
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            // ---> CONNECTING TO WALLET SERVICE <---
            try {
                // Call Wallet Service to create a wallet
                ResponseEntity<String> response = restTemplate.postForEntity(
                        WALLET_SERVICE_URL + savedUser.getId(),
                        null,
                        String.class
                );
                System.out.println("Wallet created: " + response.getBody());
            } catch (Exception e) {

                System.err.println("User registered, but Wallet creation failed: " + e.getMessage());
            }

            return savedUser;

        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            throw e;
        }
    }

    public String loginUser(String email, String rawPassword) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User Not Found. Please Register.");
            }

            // Fixed BCrypt Comparison
            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                throw new RuntimeException("Wrong password!");
            }

            String token =jwtUtil.generateToken(user.getEmail(), user.getId(), "USER");


            System.out.println("============= LOGIN SUCCESS =============");
//            System.out.println("Generated JWT Token: " + token);
            System.out.println("=========================================");

            return token;

        } catch (Exception e) {
            throw e;
        }
    }
}