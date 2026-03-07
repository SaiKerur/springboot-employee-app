package com.example.demo.service;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.SignUpRequestDTO;

public interface AuthService {

    AuthResponseDTO signUp(SignUpRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);
}
