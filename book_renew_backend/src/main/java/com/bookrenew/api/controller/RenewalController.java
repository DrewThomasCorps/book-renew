package com.bookrenew.api.controller;

import com.bookrenew.api.action.RenewalAction;
import com.bookrenew.api.entity.PotentialTrade;
import com.bookrenew.api.entity.Renewal;
import com.bookrenew.api.repository.BookUserRepository;
import com.bookrenew.api.repository.RenewalRepository;
import com.bookrenew.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class RenewalController {

    private final RenewalAction renewalAction;

    @Autowired
    public RenewalController(UserRepository userRepository, RenewalRepository renewalRepository, BookUserRepository bookUserRepository){
        this.renewalAction = new RenewalAction(userRepository, renewalRepository, bookUserRepository);
    }

    @GetMapping(produces = {"application/json"}, path = "/potential-trades")
    public List<PotentialTrade> getPotentialTrades() {
        return renewalAction.getPotentialTrades();
    }

    @GetMapping(produces = {"application/json"}, path = "/renewals")
    public List<Renewal> getRenewals() {
        return renewalAction.getRenewals();
    }

    @PostMapping(produces = {"application/json"}, path = "/renewals")
    public Renewal offerRenewal(@RequestBody HashMap<String, Long> requestData) {
        return renewalAction.offerRenewal(requestData);
    }

    @PutMapping(produces = {"application/json"}, consumes = {"application/json"}, path = "/renewals/{id}")
    public Renewal updateRenewal(@PathVariable("id") Long id, @RequestBody HashMap<String, Renewal.Status> requestData) {
        return renewalAction.updateRenewal(id, requestData);
    }

    @DeleteMapping(path = "/renewals/{id}")
    public void deleteRenewal(@PathVariable("id") String id)
    {
        renewalAction.deleteRenewal(id);
    }


}
