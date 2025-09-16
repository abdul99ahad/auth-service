package org.dev.controllers;

import lombok.RequiredArgsConstructor;
import org.dev.request.LoginRequestDTO;
import org.dev.response.LoginResponseDTO;
import org.dev.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/auth/v1/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        // TODO: add a wrapper DTO ApiResponse
        return ResponseEntity.ok(tokenService.login(loginRequestDTO));
    }
}
