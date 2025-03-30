package com.example.horse_racing.Models;

import java.time.LocalDateTime;

public class Horse {
    private int id;
    private String name;
    private int age;
    private String breed;
    private String rider;
    private LocalDateTime createAt;
    public Horse(int id, String name, int age, String breed, String rider, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.rider = rider;
        this.createAt = createAt;
    }
}
