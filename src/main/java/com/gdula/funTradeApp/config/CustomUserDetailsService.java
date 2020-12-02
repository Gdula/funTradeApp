package com.gdula.funTradeApp.config;


import com.gdula.funTradeApp.model.User;
import com.gdula.funTradeApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * class: CustomUserDetailsService
 * Reprezentuje serwis z detalami użytkownika.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * method: loadUserByUsername
     * Zwraca login użytkownika
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findFirstByLogin(username);

        if (user == null) {
            throw new UsernameNotFoundException("User does not exists!");
        }

        String role = user.getLogin().equals("admin") ? "ADMIN" : "USER";
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin())
                .roles(role)
                .password( user.getPassword())
                .build();


    }
}
