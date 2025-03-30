package com.example.horse_racing.Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bet {
    private int id;
    private int raceId;
    private int horsesId;
    private BigDecimal multiplier;
    private String betType;
    private LocalDateTime createAt;
    public Bet(int id,int raceId, int horsesId, BigDecimal multiplier, String betType, LocalDateTime createAt) {
        this.id = id;
        this.raceId = raceId;
        this.horsesId = horsesId;
        this.multiplier = multiplier;
        this.betType = betType;
        this.createAt = createAt;
    }
}
