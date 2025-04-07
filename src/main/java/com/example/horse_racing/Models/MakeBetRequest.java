package com.example.horse_racing.Models;

import java.math.BigDecimal;

public class MakeBetRequest {
    private int betId;
    private String email;
    private BigDecimal sum;

    public MakeBetRequest() {
    }

    public MakeBetRequest(int betId, String email, BigDecimal sum) {
        this.betId = betId;
        this.email = email;
        this.sum = sum;
    }

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
