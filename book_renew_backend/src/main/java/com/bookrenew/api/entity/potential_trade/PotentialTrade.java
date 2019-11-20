package com.bookrenew.api.entity.potential_trade;

import java.math.BigInteger;

public class PotentialTrade {

    private Trader trader;
    private Trader tradee;

    public PotentialTrade(BigInteger traderUserId, String traderIsbn, BigInteger traderBookUserId, String traderBookTitle, String traderName, String traderEmail,
                          BigInteger tradeeUserId, String tradeeIsbn, BigInteger tradeeBookUserId, String tradeeBookTitle, String tradeeName, String tradeeEmail) {
        this.trader = new Trader(traderName, traderUserId, traderEmail, traderBookTitle, traderIsbn, traderBookUserId);
        this.tradee = new Trader(tradeeName, tradeeUserId, tradeeEmail, tradeeBookTitle, tradeeIsbn, tradeeBookUserId);
    }

    public Trader getTradee() {
        return tradee;
    }

    public void setTradee(Trader tradee) {
        this.tradee = tradee;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }
}
