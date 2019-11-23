package com.bookrenew.api.entity;

import java.math.BigInteger;

public class PotentialTrade {


    private BookUser trader;
    private BookUser tradee;

    public PotentialTrade(BigInteger traderUserId, String traderIsbn, BigInteger traderBookUserId, String traderBookTitle, String traderName, String traderEmail,
                          BigInteger tradeeUserId, String tradeeIsbn, BigInteger tradeeBookUserId, String tradeeBookTitle, String tradeeName, String tradeeEmail) {
        this.trader = new BookUser();
        this.setupBookUser(this.trader, traderUserId, traderIsbn, traderBookUserId, traderBookTitle, traderName, traderEmail);
        this.tradee = new BookUser();
        this.setupBookUser(this.tradee, tradeeUserId, tradeeIsbn, tradeeBookUserId, tradeeBookTitle, tradeeName, tradeeEmail);
    }

    public BookUser getTradee() {
        return tradee;
    }

    public void setTradee(BookUser tradee) {
        this.tradee = tradee;
    }

    public BookUser getTrader() {
        return trader;
    }

    public void setTrader(BookUser trader) {
        this.trader = trader;
    }

    private void setupBookUser(BookUser bookUser, BigInteger UserId, String Isbn, BigInteger BookUserId, String BookTitle, String Name, String Email){
        bookUser.setId(BookUserId.longValue());
        bookUser.setStatus(BookUser.Status.library);
        User User = new User();
        User.setEmail(Email);
        User.setName(Name);
        User.setId(UserId.longValue());
        bookUser.setUser(User);
        Book Book = new Book();
        Book.setIsbn(Isbn);
        Book.setTitle(BookTitle);
        bookUser.setBook(Book);
    }
}
