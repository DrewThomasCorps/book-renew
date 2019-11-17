package com.bookrenew.api.repository;

import com.bookrenew.api.entity.User;
import com.bookrenew.api.entity.potential_trade.PotentialTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(nativeQuery = true)
    List<PotentialTrade> findPotentialTrades(Long userId);
}
