package com.bookrenew.api.entity.potential_trade;

import java.math.BigInteger;

class Trader {
    private User user;
    private Book book;
    private BigInteger book_user_id;

    Trader(String name, BigInteger id, String email, String title, String isbn, BigInteger bookUserId) {
        this.user = new User(name, id, email);
        this.book = new Book(title, isbn);
        this.book_user_id = bookUserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BigInteger getBook_user_id() {
        return book_user_id;
    }

    public void setBook_user_id(BigInteger book_user_id) {
        this.book_user_id = book_user_id;
    }
}
