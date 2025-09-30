package org.dev.services;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.dev.entities.RefreshToken;
import org.dev.entities.User;
import org.dev.exceptions.InvalidCredentialsException;
import org.dev.exceptions.UserAlreadyExistsException;
import org.dev.request.LoginRequestDTO;
import org.dev.request.RefreshTokenRequestDTO;
import org.dev.request.SignupRequestDTO;
import org.dev.response.LoginResponseDTO;
import org.dev.response.RefreshTokenResponseDTO;
import org.dev.response.SignUpResponseDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final KafkaTemplate<Integer, User> kafkaTemplate;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        // TODO: check if we even need a User in here.
        // TODO: handle via authentication manager
        // Convert loginRequestDTO to User
        User user = User.builder()
                .name(loginRequestDTO.getUsername())
                .build();
        // Check if this user already exists
        Boolean userExists = customUserDetailsService.checkIfUsernameExists(user.getName());

        if (!userExists || customUserDetailsService.checkIfPasswordsMatch(loginRequestDTO.getUsername(), passwordEncoder.encode(loginRequestDTO.getPassword()))) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        // pass this User to CustomUserDetails
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequestDTO.getUsername());
        // generateToken
        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user.getName());

        return new LoginResponseDTO(accessToken, refreshToken.getToken());

    }

    @Transactional
    public SignUpResponseDTO signup(SignupRequestDTO signupRequestDTO) {
        Boolean userExists = customUserDetailsService.checkIfUsernameExists(signupRequestDTO.getUsername());

        if (userExists) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .name(signupRequestDTO.getUsername())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .build();

        User savedUser = customUserDetailsService.signUp(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(signupRequestDTO.getUsername());
        // generateToken
        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user.getName());

        // send to kafka
        sendToKafka("signup", user);
        return new SignUpResponseDTO(accessToken, refreshToken.getToken());
    }

    public RefreshTokenResponseDTO getJwtToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        RefreshToken refreshToken = refreshTokenService
                .getActiveRefreshToken(refreshTokenRequestDTO.getRefreshToken());

        // Duplicate TODO
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(refreshToken.getUser().getName());
        String accessToken = jwtService.generateToken(userDetails);

        return new RefreshTokenResponseDTO(accessToken);
    }

    private void sendToKafka(String topic, User user) {
        ProducerRecord<Integer, User> userProducer = new ProducerRecord<>(topic, user);
        kafkaTemplate.send(userProducer);
    }
}
