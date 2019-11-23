package com.bookrenew.api.controller;

import com.bookrenew.api.entity.Renewal;
import com.bookrenew.api.entity.User;
import com.bookrenew.api.entity.potential_trade.PotentialTrade;
import com.bookrenew.api.repository.RenewalRepository;
import com.bookrenew.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository repository;
    private final RenewalRepository renewalRepository;

    @Autowired
    public UserController(UserRepository repository, PasswordEncoder passwordEncoder, RenewalRepository renewalRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.renewalRepository = renewalRepository;
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"}, path = "/register")
    public User create(@RequestBody User user) {
        if (repository.findByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email Already Exists");
        }
        if(user.getEmail().isBlank() || user.getPassword().isBlank())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Password Empty");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @GetMapping(produces = {"application/json"}, path = "/self")
    public User getAuthenticatedUser() {
        return this.getUserFromAuthCredentials();
    }

    @GetMapping(produces = {"application/json"}, path = "/potential-trades")
    public List<PotentialTrade> getPotentialTrades() {
        User user = this.getUserFromAuthCredentials();
        return repository.findPotentialTrades(user.getId());
    }

    @GetMapping(produces = {"application/json"}, path = "/renewals")
    public List<Renewal> getRenewals() {
        User user = this.getUserFromAuthCredentials();
        return renewalRepository.findRenewalsByTrader_UserOrTradee_User(user, user);
    }

    private User getUserFromAuthCredentials() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return repository.findByEmail(email);
    }

    @DeleteMapping(path = "/self")
    public void deleteUser() {
        User user = this.getUserFromAuthCredentials();
        repository.delete(user);
    }


}