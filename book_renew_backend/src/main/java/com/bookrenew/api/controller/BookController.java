package com.bookrenew.api.controller;

import com.bookrenew.api.action.BookAction;
import com.bookrenew.api.entity.Book;
import com.bookrenew.api.entity.BookUser;
import com.bookrenew.api.entity.User;
import com.bookrenew.api.repository.BookRepository;
import com.bookrenew.api.repository.BookUserRepository;
import com.bookrenew.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookAction bookAction;

    @Autowired
    public BookController(BookRepository bookRepository, BookUserRepository bookUserRepository, UserRepository userRepository) {
        this.bookAction = new BookAction(bookRepository, bookUserRepository, userRepository);
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"}, path = "/library")
    public BookUser addToLibrary(@RequestBody Book book) {
        return bookAction.addToLibrary(book);
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"}, path = "/wishlist")
    public BookUser addToWishlist(@RequestBody Book book) {
        return bookAction.addToWishlist(book);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteBookUserById(@PathVariable("id") String id){
        bookAction.deleteBookUserById(id);
    }

    @GetMapping(path = "")
    public List<BookUser> getAllBooks(){
        return bookAction.getAllBooks();
    }

}
