package com.bookrenew.api.controller;

import com.bookrenew.api.entity.User;
import com.bookrenew.api.repository.UserRepository;
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

    @GetMapping(produces = {"application/json"})
    public Map<String, Iterable<User>> findAll() {
        Map<String, Iterable<User>> userMap = new HashMap<>();
        userMap.put("users", repository.findAll());
        return userMap;
    }

    @GetMapping(path = "/{id}", produces = {"application/json"})
    public User find(@PathVariable("id") String id) {
        Long longId = Long.parseLong(id);
        return repository.findById(longId).orElse(null);
    }

    @PostMapping(consumes = "application/json", produces = {"application/json"})
    public User create(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") String id) {
        Long longId = Long.parseLong(id);
        User user = repository.findById(longId).orElse(null);
        repository.delete(user);
    }


}