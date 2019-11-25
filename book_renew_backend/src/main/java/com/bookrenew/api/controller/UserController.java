package com.bookrenew.api.controller;

import com.bookrenew.api.entity.BookUser;
import com.bookrenew.api.entity.PotentialTrade;
import com.bookrenew.api.entity.Renewal;
import com.bookrenew.api.entity.User;
import com.bookrenew.api.repository.BookUserRepository;
import com.bookrenew.api.repository.RenewalRepository;
import com.bookrenew.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final RenewalRepository renewalRepository;
    private final BookUserRepository bookUserRepository;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, RenewalRepository renewalRepository, BookUserRepository bookUserRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.renewalRepository = renewalRepository;
        this.bookUserRepository = bookUserRepository;
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"}, path = "/register")
    public User create(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email Already Exists");
        }
        if(user.getEmail().isBlank() || user.getPassword().isBlank())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Password Empty");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @GetMapping(produces = {"application/json"}, path = "/self")
    public User getAuthenticatedUser() {
        return this.getUserFromAuthCredentials();
    }

    @DeleteMapping(path = "/self")
    public void deleteUser() {
        User user = this.getUserFromAuthCredentials();
        userRepository.delete(user);
    }

    @GetMapping(produces = {"application/json"}, path = "/potential-trades")
    public List<PotentialTrade> getPotentialTrades() {
        User user = this.getUserFromAuthCredentials();
        return userRepository.findPotentialTrades(user.getId());
    }

    @GetMapping(produces = {"application/json"}, path = "/renewals")
    public List<Renewal> getRenewals() {
        User user = this.getUserFromAuthCredentials();
        return renewalRepository.findRenewalsByTrader_UserOrTradee_User(user, user);
    }

    @PostMapping(produces = {"application/json"}, path = "/renewals")
    public Renewal offerRenewal(@RequestBody HashMap<String, Long> requestData) {
        Long traderBookUserId = requestData.get("trader_book_user_id");
        Long tradeeBookUserId = requestData.get("tradee_book_user_id");
        BookUser traderBookUser = bookUserRepository.findById(traderBookUserId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trader cannot offer book that doesn't exist")
        );
        if (traderBookUser.getStatus() != BookUser.Status.library) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trader does not have book in their library.");
        }
        if (!traderBookUser.getUser().getId().equals(this.getUserFromAuthCredentials().getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Trader cannot offer book from someone else's library.");
        }
        BookUser tradeeBookUser = bookUserRepository.findById(tradeeBookUserId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trader cannot ask for book that doesn't exist")
        );
        if (tradeeBookUser.getStatus() != BookUser.Status.library) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tradee's book is not in their library");
        }
        Renewal offer = new Renewal();
        offer.setStatus(Renewal.Status.pending);
        offer.setTrader(traderBookUser);
        offer.setTradee(tradeeBookUser);
        return renewalRepository.save(offer);
    }

    private User getUserFromAuthCredentials() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email);
    }


}