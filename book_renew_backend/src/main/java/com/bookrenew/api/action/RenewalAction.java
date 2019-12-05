package com.bookrenew.api.action;

import com.bookrenew.api.entity.BookUser;
import com.bookrenew.api.entity.PotentialTrade;
import com.bookrenew.api.entity.Renewal;
import com.bookrenew.api.entity.User;
import com.bookrenew.api.repository.BookUserRepository;
import com.bookrenew.api.repository.RenewalRepository;
import com.bookrenew.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RenewalAction {

    private final UserRepository userRepository;
    private final RenewalRepository renewalRepository;
    private final BookUserRepository bookUserRepository;
    private final UserAction userAction;

    public RenewalAction(UserRepository userRepository, RenewalRepository renewalRepository, BookUserRepository bookUserRepository){
        this.userRepository = userRepository;
        this.renewalRepository = renewalRepository;
        this.bookUserRepository = bookUserRepository;
        this.userAction = new UserAction(userRepository);
    }

    public List<PotentialTrade> getPotentialTrades() {
        User user = userAction.getUserFromAuthCredentials();
        return userRepository.findPotentialTrades(user.getId());
    }

    public List<Renewal> getRenewals() {
        User user = userAction.getUserFromAuthCredentials();
        return renewalRepository.findRenewalsByTrader_UserOrTradee_User(user, user);
    }

    public Renewal offerRenewal(HashMap<String, Long> requestData) {
        Long traderBookUserId = requestData.get("trader_book_user_id");
        Long tradeeBookUserId = requestData.get("tradee_book_user_id");
        BookUser traderBookUser = bookUserRepository.findById(traderBookUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trader cannot offer book that doesn't exist")
        );
        BookUser tradeeBookUser = bookUserRepository.findById(tradeeBookUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trader cannot ask for book that doesn't exist")
        );
        validateTrade(traderBookUser, tradeeBookUser);
        return createTrade(traderBookUser, tradeeBookUser);
    }

    public Renewal updateRenewal(Long id, HashMap<String, Renewal.Status> requestData) {
        Renewal.Status status = requestData.get("status");
        Renewal renewal = renewalRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: renewal does not exist")
        );
        if (status == Renewal.Status.completed) {
            tradeBooks(renewal);
        }
        renewal.setStatus(status);
        return renewalRepository.save(renewal);
    }

    public void deleteRenewal(String id)
    {
        Long longId = Long.parseLong(id);
        Renewal renewal = renewalRepository.findById(longId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "ID does not exist"));
        renewalRepository.delete(renewal);
    }

    private void validateTrade(BookUser traderBookUser, BookUser tradeeBookUser) {
        if (traderBookUser.getStatus() != BookUser.Status.library) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trader does not have book in their library.");
        }
        if (!traderBookUser.getUser().getId().equals(userAction.getUserFromAuthCredentials().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Trader cannot offer book from someone else's library.");
        }

        if (tradeeBookUser.getStatus() != BookUser.Status.library) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tradee's book is not in their library");
        }
    }

    private Renewal createTrade(BookUser traderBookUser, BookUser tradeeBookUser) {
        Renewal offer = new Renewal();
        offer.setStatus(Renewal.Status.pending);
        offer.setTrader(traderBookUser);
        offer.setTradee(tradeeBookUser);
        return renewalRepository.save(offer);
    }

    private void tradeBooks(Renewal renewal) {
        BookUser trader = renewal.getTrader();
        BookUser tradee = renewal.getTradee();
        trader.setStatus(BookUser.Status.traded);
        tradee.setStatus(BookUser.Status.traded);
        List<BookUser> bookUsers = new ArrayList<>();
        bookUsers.add(trader);
        bookUsers.add(tradee);
        bookUserRepository.saveAll(bookUsers);
    }


}
