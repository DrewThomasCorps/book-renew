package com.bookrenew.api.controller;

import com.bookrenew.api.entity.Book;
import com.bookrenew.api.entity.BookUser;
import com.bookrenew.api.entity.User;
import com.bookrenew.api.repository.BookRepository;
import com.bookrenew.api.repository.BookUserRepository;
import com.bookrenew.api.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"}, path="/owned")
    public Book create(@RequestBody Book book){
        Book createdBook = this.bookRepository.save(book);
        User user = this.getUserFromAuthCredentials();
        BookUser bookUser = new BookUser();
        bookUser.setBook(createdBook);
        bookUser.setUser(user);
        bookUser.setStatus(BookUser.Status.owner);
        bookUserRepository.save(bookUser);
        return createdBook;
    }

    private User getUserFromAuthCredentials() {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email);
    }

}
