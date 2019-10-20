package com.bookrenew.api.entity;

import com.bookrenew.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface uniqueCheck extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
