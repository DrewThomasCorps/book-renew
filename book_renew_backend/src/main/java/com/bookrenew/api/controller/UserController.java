package com.bookrenew.api.controller;

import com.bookrenew.api.entity.User;
import com.bookrenew.api.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository repository;

    public UserController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(consumes = "application/json", produces = {"application/json"}, path="/register")
    public User create(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @GetMapping(produces = {"application/json"}, path = "/self")
    public User getAuthenticatedUser() {
        return this.getUserFromAuthCredentials();
    }

    @DeleteMapping(path = "/self")
    public void deleteUser() {
        User user = this.getUserFromAuthCredentials();
        repository.delete(user);
    }

    private User getUserFromAuthCredentials() {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return repository.findByEmail(email);
    }

    @GetMapping(produces = {"application/json"})
    public Map<String, String> findByEmail() {Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Map<String, String> userMap = new HashMap<>();
        if(repository.findByEmail(email) == null)
        {
            userMap.put("error", "200");
        }
        else
        {
            userMap.put("error", "409");
        }
        return userMap;
    }
}