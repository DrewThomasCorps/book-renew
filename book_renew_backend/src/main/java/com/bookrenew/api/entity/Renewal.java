package com.bookrenew.api.entity;

import javax.persistence.*;

@Entity
public class Renewal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trader_book_user_id")
    private BookUser trader;

    @ManyToOne
    @JoinColumn(name = "tradee_book_user_id")
    private BookUser tradee;

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

    public BookUser getTrader() {
        return trader;
    }

    public void setTrader(BookUser traderBookUser) {
        this.trader = traderBookUser;
    }

    public BookUser getTradee() {
        return tradee;
    }

    public void setTradee(BookUser tradeeBookUser) {
        this.tradee = tradeeBookUser;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
