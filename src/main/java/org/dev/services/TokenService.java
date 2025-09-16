package org.dev.services;

import lombok.RequiredArgsConstructor;
import org.dev.entities.RefreshToken;
import org.dev.entities.User;
import org.dev.request.LoginRequestDTO;
import org.dev.response.LoginResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        // Convert loginRequestDTO to User
        User user = User.builder()
                .name(loginRequestDTO.getUsername())
                .build();
        // Check if this user already exists
        Boolean userExists = customUserDetailsService.checkIfUsernameExists(user.getName());

        if ( !userExists || !passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        // pass this User to CustomUserDetails
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequestDTO.getUsername());
        // generateToken
        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user.getName());

        return new LoginResponseDTO(accessToken, refreshToken.getToken());

    }
}
