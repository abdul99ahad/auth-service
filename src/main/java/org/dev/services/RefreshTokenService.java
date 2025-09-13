package org.dev.services;

import lombok.RequiredArgsConstructor;
import org.dev.entities.RefreshToken;
import org.dev.entities.User;
import org.dev.repositories.RefreshTokenRepository;
import org.dev.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Autowired // Cuz it's already injecting when we use final with @RequiredArgsConstructor
    private final UserRepository userRepository;

    @Autowired
    private final RefreshTokenRepository tokenRepository;

    public RefreshToken generateRefreshToken(String username) {
        User user = userRepository.findByName(username);
        RefreshToken refreshToken = RefreshToken
                            .builder()
                            .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .build();

        return tokenRepository.save(refreshToken);
    }

    // TODO: is refresh token valid
}
