package com.bookrenew.api.repository;

import com.bookrenew.api.entity.BookUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface BookUserRepository extends JpaRepository<BookUser, Long> {
}
