package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.SignUpRequestDTO;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> signUp(@Valid @RequestBody SignUpRequestDTO dto) {
        AuthResponseDTO user = authService.signUp(dto);
        ApiResponse<AuthResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "User registered successfully",
                user
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO dto) {
        AuthResponseDTO user = authService.login(dto);
        ApiResponse<AuthResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Login successful",
                user
        );
        return ResponseEntity.ok(response);
    }
}
