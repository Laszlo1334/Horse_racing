package com.example.horse_racing.Models;

import java.time.LocalDateTime;

public class Race {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private LocalDateTime createAt;

    public Race(int id, LocalDateTime startTime, LocalDateTime endTime, String location, LocalDateTime createAt) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.createAt = createAt;
    }
}
