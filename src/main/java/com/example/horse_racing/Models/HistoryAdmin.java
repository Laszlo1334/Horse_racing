package com.example.horse_racing.Models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class HistoryAdmin {
    private int placedBetId;
    private int betId;
    private int userId;
    private BigDecimal sum;
    private String state;
    private BigDecimal multiplier;
    private Timestamp createdAt;

    public HistoryAdmin(int placedBetId, int betId, int userId, BigDecimal sum, String state, BigDecimal multiplier, Timestamp createdAt) {
        this.placedBetId = placedBetId;
        this.betId = betId;
        this.userId = userId;
        this.sum = sum;
        this.state = state;
        this.multiplier = multiplier;
        this.createdAt = createdAt;
    }

    public int getPlacedBetId() {
        return placedBetId;
    }

    public void setPlacedBetId(int placedBetId) {
        this.placedBetId = placedBetId;
    }

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
