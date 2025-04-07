package com.example.horse_racing.Models;

import java.sql.Timestamp;

public class Horse {
    private int id;
    private String name;
    private int age;
    private String breed;
    private String rider;
    private Timestamp createAt;

    public Horse(int id, String name, int age, String breed, String rider, Timestamp createAt) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.rider = rider;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getBreed() {
        return breed;
    }

    public String getRider() {
        return rider;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }
}
