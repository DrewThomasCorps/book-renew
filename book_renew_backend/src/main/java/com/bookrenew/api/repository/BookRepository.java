package com.bookrenew.api.repository;

import com.bookrenew.api.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(exported = false)
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByIsbn(String isbn);
}
