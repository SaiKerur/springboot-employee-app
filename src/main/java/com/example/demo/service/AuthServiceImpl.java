package com.example.demo.service;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.SignUpRequestDTO;
import com.example.demo.exception.AuthenticationFailedException;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.model.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponseDTO signUp(SignUpRequestDTO request) {
        userAccountRepository.findByEmailIgnoreCase(request.email()).ifPresent(existing -> {
            throw new DuplicateResourceException("User already exists with email: " + request.email());
        });

        UserAccount user = new UserAccount();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        UserAccount savedUser = userAccountRepository.save(user);
        log.info("User signed up with id: {}", savedUser.getId());
        return toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO request) {
        UserAccount user = userAccountRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new AuthenticationFailedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthenticationFailedException("Invalid email or password");
        }

        log.info("User logged in with id: {}", user.getId());
        return toResponse(user);
    }

    private AuthResponseDTO toResponse(UserAccount user) {
        return new AuthResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
