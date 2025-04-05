package com.example.horse_racing.Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class BetListDto {
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

    // Constructors
    public BetListDto(int betId, int raceId, Timestamp raceStartTime, Timestamp raceEndTime,
                      String raceLocation, int horseId, String horseName, int horseAge, String horseBreed,
                      String horseRider, BigDecimal multyplier, String betType) {
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
        System.out.println("Object is made of BetListDto");
    }

    public int getBetId() { return betId; }
    public void setBetId(int betId) { this.betId = betId; }

    public int getRaceId() { return raceId; }
    public void setRaceId(int raceId) { this.raceId = raceId; }

    public Timestamp getRaceStartTime() { return raceStartTime; }
    public void setRaceStartTime(Timestamp raceStartTime) { this.raceStartTime = raceStartTime; }

    public Timestamp getRaceEndTime() { return raceEndTime; }
    public void setRaceEndTime(Timestamp raceEndTime) { this.raceEndTime = raceEndTime; }

    public String getRaceLocation() { return raceLocation; }
    public void setRaceLocation(String raceLocation) { this.raceLocation = raceLocation; }

    public int getHorseId() { return horseId; }
    public void setHorseId(int horseId) { this.horseId = horseId; }

    public String getHorseName() { return horseName; }
    public void setHorseName(String horseName) { this.horseName = horseName; }

    public int getHorseAge() { return horseAge; }
    public void setHorseAge(int horseAge) { this.horseAge = horseAge; }

    public String getHorseBreed() { return horseBreed; }
    public void setHorseBreed(String horseBreed) { this.horseBreed = horseBreed; }

    public String getHorseRider() { return horseRider; }
    public void setHorseRider(String horseRider) { this.horseRider = horseRider; }

    public BigDecimal getMultyplier() { return multyplier; }
    public void setMultyplier(BigDecimal multyplier) { this.multyplier = multyplier; }

    public String getBetType() { return betType; }
    public void setBetType(String betType) { this.betType = betType; }
}
