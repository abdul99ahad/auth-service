package org.dev.repositories;

import org.dev.entities.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserUserIdAndActiveTrue(String userId);
    Optional<RefreshToken> findByTokenAndActiveTrue(String token);
}
