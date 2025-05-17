package com.svalero.musicApi.service;

import com.svalero.musicApi.domain.User;
import com.svalero.musicApi.exception.PasswordIncorrectException;
import com.svalero.musicApi.exception.UserNotFoundException;
import com.svalero.musicApi.repository.UserRepository;
import com.svalero.musicApi.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = new JwtUtil();
    }

    public String login(String email, String password) throws UserNotFoundException, PasswordIncorrectException {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordIncorrectException("Invalid credentials");
        }

        return this.jwtUtil.generateToken(user.getEmail());
    }
}
