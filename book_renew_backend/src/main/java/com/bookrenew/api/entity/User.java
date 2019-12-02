package com.bookrenew.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Set;


@NamedNativeQuery(name = "User.findPotentialTrades",
        resultSetMapping = "potential-trades",
        query = "SELECT " +
                "    trader.user_id as traderUserId, " +
                "    trader.isbn as traderIsbn, " +
                "    trader.id as traderBookUserId, " +
                "    trader_books.title as traderBookName, " +
                "    trader_user.name as traderName, " +
                "    trader_user.email as traderEmail, " +
                "    tradee.tradee_user_id as tradeeUserId, " +
                "    tradee.tradee_isbn as tradeeIsbn, " +
                "    tradee.tradee_book_user_id as tradeeBookUserId, " +
                "    tradee_book.title as tradeeBookName, " +
                "    tradee_user.name as tradeeName, " +
                "    tradee_user.email as tradeeEmail " +
                "FROM  " +
                "    ( " +
                "    SELECT " +
                "        haves.id AS tradee_book_user_id, " +
                "        haves.isbn AS tradee_isbn, " +
                "        haves.user_id AS tradee_user_id, " +
                "        haves.status AS tradee_book_user_status, " +
                "        wants.isbn AS wantsIsbn " +
                "    FROM " +
                "        ( " +
                "        SELECT " +
                "            * " +
                "        FROM " +
                "            book_user " +
                "        WHERE " +
                "            book_user.status = 'wishlist' AND isbn IN( " +
                "            SELECT " +
                "                isbn " +
                "            FROM " +
                "                book_user " +
                "            WHERE " +
                "                book_user.user_id = ?1 AND " +
                "            `status` = 'library' " +
                "        ) " +
                "    ) AS wants  " +
                "JOIN( " +
                "    SELECT " +
                "        * " +
                "    FROM " +
                "        book_user " +
                "    WHERE " +
                "        book_user.status = 'library' AND isbn IN( " +
                "        SELECT " +
                "            isbn " +
                "        FROM " +
                "            book_user " +
                "        WHERE " +
                "            user_id = ?1 AND " +
                "            `status` = 'wishlist' " +
                "    ) " +
                ") AS haves  " +
                "ON " +
                "    wants.user_id = haves.user_id " +
                ") AS tradee " +
                "JOIN book_user AS trader  " +
                "ON " +
                "    trader.isbn = tradee.wantsIsbn AND " +
                "    `status` = 'library' AND " +
                "    `user_id` = ?1 " +
                "JOIN users AS trader_user ON trader.user_id = trader_user.id " +
                "JOIN book AS trader_books ON trader.isbn = trader_books.isbn " +
                "JOIN users AS tradee_user ON tradee.tradee_user_id = tradee_user.id " +
                "JOIN book AS tradee_book ON tradee.tradee_isbn = tradee_book.isbn ")
@SqlResultSetMapping(name = "potential-trades", classes = {
        @ConstructorResult(targetClass = PotentialTrade.class, columns = {
                @ColumnResult(name = "traderUserId", type = BigInteger.class),
                @ColumnResult(name = "traderIsbn", type = String.class),
                @ColumnResult(name = "traderBookUserId", type = BigInteger.class),
                @ColumnResult(name = "traderBookName", type = String.class),
                @ColumnResult(name = "traderName", type = String.class),
                @ColumnResult(name = "traderEmail", type = String.class),
                @ColumnResult(name = "tradeeUserId", type = BigInteger.class),
                @ColumnResult(name = "tradeeIsbn", type = String.class),
                @ColumnResult(name = "tradeeBookUserId", type = BigInteger.class),
                @ColumnResult(name = "tradeeBookName", type = String.class),
                @ColumnResult(name = "tradeeName", type = String.class),
                @ColumnResult(name = "tradeeEmail", type = String.class)
        })
})
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<BookUser> bookUsers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BookUser> getBookUsers() {
        return bookUsers;
    }

    public void setBookUsers(Set<BookUser> bookUsers) {
        this.bookUsers = bookUsers;
    }

}
