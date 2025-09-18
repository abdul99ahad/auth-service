package org.dev.services;

import lombok.RequiredArgsConstructor;
import org.dev.adapters.CustomUserDetails;
import org.dev.entities.User;
import org.dev.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user, passwordEncoder);
    }

    public Boolean checkIfUsernameExists(String username) {
        return userRepository.findByName(username) != null;
    }

    public Boolean checkIfPasswordsMatch(String username, String password) {
        User user = userRepository.findByName(username);
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User signUp(User user) {
        return userRepository.save(user);
    }
}
