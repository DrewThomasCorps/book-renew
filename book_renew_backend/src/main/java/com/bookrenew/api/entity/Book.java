package com.bookrenew.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Book {

    @Id
    @Column(name = "isbn", updatable = false, nullable = false, length = 13)
    private String isbn;

    @Column(name = "title", updatable = false, nullable = false)
    private String title;

    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<BookUser> bookUsers;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<BookUser> getBookUsers() {
        return bookUsers;
    }

    public void setBookUsers(Set<BookUser> bookUsers) {
        this.bookUsers = bookUsers;
    }
}
