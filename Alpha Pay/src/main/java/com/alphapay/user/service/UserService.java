package com.alphapay.user.service;

import com.alphapay.user.entity.User;
import com.alphapay.user.repository.UserRepository;
import com.alphapay.user.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {
        try {

            User existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser != null) {
                throw new RuntimeException("Email Register Already!");
            }


            user.setPassword(passwordEncoder.encode(user.getPassword()));


            return userRepository.save(user);

        } catch (Exception e) {

            System.err.println("Registration got an error: " + e.getMessage());
            throw e;
        }

    }

    @Autowired
    private JwtUtil jwtUtil;

    public String loginUser(String email, String rawPassword) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User Not Found.. Plz Register");
            }


            if (!user.getPassword().equals(rawPassword)) {
                throw new RuntimeException("Wrong password!");
            }


            return jwtUtil.generateToken(user.getEmail(), user.getId(), "USER");

        } catch (Exception e) {
            throw e;
        }
    }
}