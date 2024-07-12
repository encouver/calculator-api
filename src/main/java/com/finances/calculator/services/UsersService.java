package com.finances.calculator.services;

import com.finances.calculator.dto.UserDTO;
import com.finances.calculator.entities.User;
import com.finances.calculator.enums.StatusEnum;
import com.finances.calculator.repositories.UserRepository;
import com.finances.calculator.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finances.calculator.authentication.UserDetailsServiceImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Marcos Ramirez
 */
@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public String loginUser(UserDTO userDTO) throws Exception {
        Optional<User> user =  userRepository.findByUsername(userDTO.getUsername().trim());
        List<User> users = userRepository.findAll();

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (Objects.equals(user.get().getStatus(), StatusEnum.INACTIVE.name().toLowerCase())) {
            throw new IllegalArgumentException("User is inactive");
        }

        String token = "";
        try {
            if (passwordEncoder.matches(userDTO.getPassword(), user.get().getPassword())) {
                // Passwords match, authenticate the user
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
                );
            } else {
                throw new BadCredentialsException("INVALID_CREDENTIALS");
            }


            final UserDetails userDetails = userDetailsServiceImpl
                    .loadUserByUsername(userDTO.getUsername());

            token = jwtUtil.generateToken(userDetails);

        } catch (Exception e) {
            e.printStackTrace();
            token = null;
        }

        return token;

    }

    private void authenticate(String username, String password) throws Exception {
        try {
            // Encode the password using the configured password encoder.
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Transactional
    public Boolean logoutUser(UserDTO logoutRequest) {
        return true;
    }

    @Transactional
    public void registerUser(UserDTO registerRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}