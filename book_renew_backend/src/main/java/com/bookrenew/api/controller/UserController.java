package com.bookrenew.api.controller;

import com.bookrenew.api.action.UserAction;
import com.bookrenew.api.entity.User;
import com.bookrenew.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserAction userAction;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userAction = new UserAction(userRepository);
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"}, path = "/register")
    public User create(@RequestBody User user) {
        return userAction.create(user, passwordEncoder);
    }

    @GetMapping(produces = {"application/json"}, path = "/self")
    public User getAuthenticatedUser() {
        return userAction.getUserFromAuthCredentials();
    }

    @DeleteMapping(path = "/self")
    public void deleteUser() {
        userAction.deleteAuthenticatedUser();
    }


}