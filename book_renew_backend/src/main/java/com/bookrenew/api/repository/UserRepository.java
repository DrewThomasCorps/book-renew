package com.bookrenew.api.repository;

import com.bookrenew.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/users")
public interface UserRepository extends JpaRepository<User, String> {

}
