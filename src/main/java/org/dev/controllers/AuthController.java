package org.dev.controllers;

import lombok.RequiredArgsConstructor;
import org.dev.request.LoginRequestDTO;
import org.dev.request.SignupRequestDTO;
import org.dev.response.LoginResponseDTO;
import org.dev.response.SignUpResponseDTO;
import org.dev.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;

    @PostMapping("/auth/v1/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        // TODO: add a wrapper DTO ApiResponse
        return ResponseEntity.ok(tokenService.login(loginRequestDTO));
    }

    @PostMapping("/auth/v1/signup")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody SignupRequestDTO signupRequestDTO) {
        return ResponseEntity.ok(tokenService.signup(signupRequestDTO));
    }
}
