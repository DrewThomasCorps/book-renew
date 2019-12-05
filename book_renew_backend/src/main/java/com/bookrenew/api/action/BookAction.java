package com.bookrenew.api.action;

import com.bookrenew.api.entity.Book;
import com.bookrenew.api.entity.BookUser;
import com.bookrenew.api.entity.User;
import com.bookrenew.api.repository.BookRepository;
import com.bookrenew.api.repository.BookUserRepository;
import com.bookrenew.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class BookAction {

    private final BookRepository bookRepository;
    private final BookUserRepository bookUserRepository;
    private final UserAction userAction;

    public BookAction(BookRepository bookRepository, BookUserRepository bookUserRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.bookUserRepository = bookUserRepository;
        this.userAction = new UserAction(userRepository);
    }

    public BookUser addToLibrary(Book book) {
        Book createdBook = this.createBook(book);
        BookUser bookUser = this.setupBookUser(createdBook);
        bookUser.setStatus(BookUser.Status.library);
        return bookUserRepository.save(bookUser);
    }

    public BookUser addToWishlist(Book book) {
        Book createdBook = this.createBook(book);
        BookUser bookUser = this.setupBookUser(createdBook);
        bookUser.setStatus(BookUser.Status.wishlist);
        return bookUserRepository.save(bookUser);
    }

    public void deleteBookUserById(String id){
        Long longId = Long.parseLong(id);
        BookUser bookUser = bookUserRepository.findById(longId).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID does not exist")
        );
        User user = userAction.getUserFromAuthCredentials();
        if (!user.getEmail().equals(bookUser.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Book belongs to different user");
        }
        bookUserRepository.delete(bookUser);
    }

    public List<BookUser> getAllBooks(){
        User user = userAction.getUserFromAuthCredentials();
        return bookUserRepository.findByUser_id(user.getId());
    }


    private Book createBook(Book book) {
        Book createdBook = book;
        if (bookRepository.findByIsbn(book.getIsbn()) == null) {
            createdBook = bookRepository.save(book);
        }
        return createdBook;
    }

    private BookUser setupBookUser(Book book) {
        User user = userAction.getUserFromAuthCredentials();
        BookUser bookUser = new BookUser();
        bookUser.setBook(book);
        bookUser.setUser(user);
        return bookUser;
    }
}
