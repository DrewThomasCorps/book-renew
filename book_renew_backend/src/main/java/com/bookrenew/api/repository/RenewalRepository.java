package com.bookrenew.api.repository;

import com.bookrenew.api.entity.Renewal;
import com.bookrenew.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(exported = false)
public interface RenewalRepository extends JpaRepository<Renewal, Long> {
    List<Renewal> findRenewalsByTrader_UserOrTradee_User(User trader, User tradee);
}