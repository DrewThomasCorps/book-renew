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

    private void setupBookUser(BookUser bookUser, BigInteger userId, String isbn, BigInteger bookUserId, String title, String name, String email){
        bookUser.setId(bookUserId.longValue());
        bookUser.setStatus(BookUser.Status.library);
        setupUser(bookUser, userId, name, email);
        setupBook(bookUser, isbn, title);
    }

    private void setupUser(BookUser bookUser, BigInteger id, String name, String email){
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setId(id.longValue());
        bookUser.setUser(user);
    }

    private void setupBook(BookUser bookUser, String isbn, String title) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(title);
        bookUser.setBook(book);
    }

}
