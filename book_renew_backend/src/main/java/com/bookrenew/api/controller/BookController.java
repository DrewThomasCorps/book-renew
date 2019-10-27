package com.bookrenew.api.controller;

import com.bookrenew.api.entity.Book;
import com.bookrenew.api.entity.BookUser;
import com.bookrenew.api.entity.User;
import com.bookrenew.api.repository.BookRepository;
import com.bookrenew.api.repository.BookUserRepository;
import com.bookrenew.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;
    private final BookUserRepository bookUserRepository;
    private final UserRepository userRepository;

    public BookController(BookRepository bookRepository, BookUserRepository bookUserRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.bookUserRepository = bookUserRepository;
        this.userRepository = userRepository;
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"}, path = "/owned")
    public BookUser addToOwned(@RequestBody Book book) {
        Book createdBook = this.createBook(book);
        BookUser bookUser = this.setupBookUser(createdBook);
        bookUser.setStatus(BookUser.Status.owner);
        return bookUserRepository.save(bookUser);
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"}, path = "/wishlist")
    public BookUser addToWishlist(@RequestBody Book book) {
        Book createdBook = this.createBook(book);
        BookUser bookUser = this.setupBookUser(createdBook);
        bookUser.setStatus(BookUser.Status.wishlist);
        return bookUserRepository.save(bookUser);
    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteBookUserById(@PathVariable("id") String id){
        Long longId = Long.parseLong(id);
        BookUser bookUser = bookUserRepository.findById(longId).orElse(new BookUser());
        User user = this.getUserFromAuthCredentials();
        if (!user.getEmail().equals(bookUser.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Book belongs to different user");
        }
        bookUserRepository.delete(bookUser);
    }

    private Book createBook(Book book) {
        Book createdBook = book;
        if (bookRepository.findByIsbn(book.getIsbn()) == null) {
            createdBook = bookRepository.save(book);
        }
        return createdBook;
    }

    private User getUserFromAuthCredentials() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email);
    }

    private BookUser setupBookUser(Book book) {
        User user = this.getUserFromAuthCredentials();
        BookUser bookUser = new BookUser();
        bookUser.setBook(book);
        bookUser.setUser(user);
        return bookUser;
    }

}
