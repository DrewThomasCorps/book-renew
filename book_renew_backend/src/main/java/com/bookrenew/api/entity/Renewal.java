package com.bookrenew.api.entity;

import javax.persistence.*;

@Entity
public class Renewal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trader_book_user_id")
    private BookUser traderBookUser;

    @ManyToOne
    @JoinColumn(name = "tradee_book_user_id")
    private BookUser tradeeBookUser;

    @ManyToOne
    @JoinColumn(name = "trader_id")
    private User trader;

    @ManyToOne
    @JoinColumn(name = "tradee_id")
    private User tradee;

    public enum Status {
        pending,
        active,
        declined,
        completed
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('pending', 'active', 'declined', 'completed')")
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookUser getTraderBookUser() {
        return traderBookUser;
    }

    public void setTraderBookUser(BookUser traderBookUser) {
        this.traderBookUser = traderBookUser;
    }

    public BookUser getTradeeBookUser() {
        return tradeeBookUser;
    }

    public void setTradeeBookUser(BookUser tradeeBookUser) {
        this.tradeeBookUser = tradeeBookUser;
    }

    public User getTrader() {
        return trader;
    }

    public void setTrader(User trader) {
        this.trader = trader;
    }

    public User getTradee() {
        return tradee;
    }

    public void setTradee(User tradee) {
        this.tradee = tradee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
