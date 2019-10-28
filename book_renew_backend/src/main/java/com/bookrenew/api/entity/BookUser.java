package com.bookrenew.api.entity;

import javax.persistence.*;

@Entity
public class BookUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "isbn")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public enum Status {
        wishlist,
        owner,
        traded
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('wishlist', 'owner', 'traded')")
    private Status status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
