package org.dev.services;

import lombok.RequiredArgsConstructor;
import org.dev.entities.RefreshToken;
import org.dev.entities.User;
import org.dev.exceptions.InvalidRefreshTokenException;
import org.dev.repositories.RefreshTokenRepository;
import org.dev.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Autowired // Cuz it's already injecting when we use final with @RequiredArgsConstructor
    private final UserRepository userRepository;

    @Autowired
    private final RefreshTokenRepository tokenRepository;

    private void makeAllRefreshTokensInvalid(String userId) {
       Optional<RefreshToken> activeRefreshToken = tokenRepository.findByUserUserIdAndActiveTrue(userId);
       if (activeRefreshToken.isPresent()) {
           activeRefreshToken.get().setActive(false);
           tokenRepository.save(activeRefreshToken.get());
       }
    }

    private Boolean isRefreshTokenValid(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().after(new Date());
    }

    public RefreshToken generateRefreshToken(String username) {
        User user = userRepository.findByName(username);
        makeAllRefreshTokensInvalid(user.getUserId());
        RefreshToken refreshToken = RefreshToken
                .builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .active(true)
                .expiryDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .build();

        return tokenRepository.save(refreshToken);
    }

    public RefreshToken getActiveRefreshToken(String token) {
        Optional<RefreshToken> r = tokenRepository.findByTokenAndActiveTrue(token);

        return tokenRepository.findByTokenAndActiveTrue(token)
                .filter(this::isRefreshTokenValid)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token not found"));

    }

}
