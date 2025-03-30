package com.example.horse_racing.Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PlacedBet {
    private int id;
    private int betId;
    private int userId;
    private String state;
    private BigDecimal sum;
    private LocalDateTime createAt;

    public PlacedBet(int id, int betId, int userId, String state, BigDecimal sum, LocalDateTime createAt) {
        this.id = id;
        this.betId = betId;
        this.userId = userId;
        this.state = state;
        this.sum = sum;
        this.createAt = createAt;
    }
}
