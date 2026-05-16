package com.alphapay.user.service;

import com.alphapay.user.entity.User;
import com.alphapay.user.repository.UserRepository;
import com.alphapay.user.utils.JwtUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Balanced Eureka Service Discovery URL
    private final String WALLET_SERVICE_URL = "http://wallet-service/alphapay/wallet/create?userId=";

    /**
     * Registers a new user and communicates with Wallet Service using a Circuit Breaker.
     * If the wallet service fails consecutively, the circuit trips and opens the fallback mechanism.
     */
    @CircuitBreaker(name = "walletServiceCB", fallbackMethod = "walletFallback")
    public User registerUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Email already registered!");
        }

        // Encode password and save user to main database
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Call Wallet Service directly via load-balanced RestTemplate
        // Internal try-catch removed so Resilience4j can capture the communication exceptions
        ResponseEntity<String> response = restTemplate.postForEntity(
                WALLET_SERVICE_URL + savedUser.getId(),
                null,
                String.class
        );
        
        System.out.println("Wallet created successfully: " + response.getBody());
        return savedUser;
    }

    /**
     * Fallback method executed automatically when wallet-service is down or circuit is open.
     * Note: Parameters must match the target method exactly, followed by a Throwable exception parameter.
     */
    public User walletFallback(User user, Throwable throwable) {
        System.err.println("===> CRITICAL: WALLET SERVICE IS UNREACHABLE! EXECUTING FALLBACK <===");
        System.err.println("Failure Context: " + throwable.getMessage());
        
        // Hide password hash strings from client response for production standards
        user.setPassword("HIDDEN_FOR_SECURITY");
        
        // Return user context safely so the core registration process completes successfully
        return user;
    }

    /**
     * Authentic and verifies user payload strings against stored credentials.
     */
    public String loginUser(String email, String rawPassword) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User Not Found. Please Register.");
            }

            // Secure BCrypt structural evaluation mapping
            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                throw new RuntimeException("Wrong password!");
            }

            String token = jwtUtil.generateToken(user.getEmail(), user.getId(), "USER");

            System.out.println("============= LOGIN SUCCESS =============");
            System.out.println("=========================================");

            return token;

        } catch (Exception e) {
            throw e;
        }
    }
}
