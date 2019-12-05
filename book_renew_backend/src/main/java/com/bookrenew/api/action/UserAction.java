package com.bookrenew.api.action;

import com.bookrenew.api.entity.User;
import com.bookrenew.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

public class UserAction {

    private final UserRepository userRepository;

    public UserAction(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User create(User user, PasswordEncoder passwordEncoder) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email Already Exists");
        }
        if (user.getEmail().isBlank() || user.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Password Empty");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteAuthenticatedUser() {
        User user = this.getUserFromAuthCredentials();
        userRepository.delete(user);
    }

    public User getUserFromAuthCredentials() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email);
    }
}
