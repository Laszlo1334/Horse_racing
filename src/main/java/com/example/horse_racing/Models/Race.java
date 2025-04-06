package com.example.horse_racing.Models;

import java.sql.Timestamp;

public class Race {
    private int id;
    private Timestamp startTime;
    private Timestamp endTime;
    private String location;
    private Timestamp createAt;

    public Race(int id, Timestamp startTime, Timestamp endTime, String location, Timestamp createAt) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }
}
