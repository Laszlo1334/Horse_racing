package com.example.horse_racing.Models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UserHistory {
    private int placedBetId;
    private int userId;
    private int betId;
    private int raceId;
    private Timestamp raceStartTime;
    private Timestamp raceEndTime;
    private String raceLocation;
    private int horseId;
    private String horseName;
    private int horseAge;
    private String horseBreed;
    private String horseRider;
    private BigDecimal multyplier;
    private String betType;
    private BigDecimal sum;
    private String state;
    private Timestamp placedAt;

    public UserHistory(int placedBetId, int userId, int betId, int raceId, Timestamp raceStartTime, Timestamp raceEndTime,
                       String raceLocation, int horseId, String horseName, int horseAge, String horseBreed,
                       String horseRider, BigDecimal multyplier, String betType,
                       BigDecimal sum, String state, Timestamp placedAt) {
        this.placedBetId = placedBetId;
        this.userId = userId;
        this.betId = betId;
        this.raceId = raceId;
        this.raceStartTime = raceStartTime;
        this.raceEndTime = raceEndTime;
        this.raceLocation = raceLocation;
        this.horseId = horseId;
        this.horseName = horseName;
        this.horseAge = horseAge;
        this.horseBreed = horseBreed;
        this.horseRider = horseRider;
        this.multyplier = multyplier;
        this.betType = betType;
        this.sum = sum;
        this.state = state;
        this.placedAt = placedAt;
    }

    public int getPlacedBetId() { return placedBetId; }
    public int getUserId() { return userId; }
    public int getBetId() { return betId; }
    public int getRaceId() { return raceId; }
    public Timestamp getRaceStartTime() { return raceStartTime; }
    public Timestamp getRaceEndTime() { return raceEndTime; }
    public String getRaceLocation() { return raceLocation; }
    public int getHorseId() { return horseId; }
    public String getHorseName() { return horseName; }
    public int getHorseAge() { return horseAge; }
    public String getHorseBreed() { return horseBreed; }
    public String getHorseRider() { return horseRider; }
    public BigDecimal getMultyplier() { return multyplier; }
    public String getBetType() { return betType; }
    public BigDecimal getSum() { return sum; }
    public String getState() { return state; }
    public Timestamp getPlacedAt() { return placedAt; }

}
