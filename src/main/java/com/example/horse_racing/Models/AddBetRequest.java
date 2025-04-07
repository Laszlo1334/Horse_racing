package com.example.horse_racing.Models;

import java.math.BigDecimal;

public class AddBetRequest {
    private int raceId;
    private int horseId;
    private BigDecimal multiplier;
    private String betType;

    public AddBetRequest() {}

    public AddBetRequest(int raceId, int horseId, BigDecimal multiplier, String betType) {
        this.raceId = raceId;
        this.horseId = horseId;
        this.multiplier = multiplier;
        this.betType = betType;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType;
    }
}
