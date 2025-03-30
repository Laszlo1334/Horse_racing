package com.example.horse_racing.Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private BigDecimal wallet;
    private LocalDateTime createAt;

    public User(int id, String username, String password, String role, BigDecimal wallet, LocalDateTime createAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.wallet = wallet;
        this.createAt = createAt;
    }
}
