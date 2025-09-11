package org.dev.services;

import lombok.RequiredArgsConstructor;
import org.dev.adapters.CustomUserDetails;
import org.dev.entities.User;
import org.dev.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username)
                .orElseThrow(() ->  new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
