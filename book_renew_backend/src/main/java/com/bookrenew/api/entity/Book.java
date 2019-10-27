package com.bookrenew.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Book {

    @Id
    @Column(name = "isbn", updatable = false, nullable = false, length = 13)
    private String isbn;

    @Column(name = "title", updatable = false, nullable = false)
    private String title;

    @OneToMany(mappedBy = "book")
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
