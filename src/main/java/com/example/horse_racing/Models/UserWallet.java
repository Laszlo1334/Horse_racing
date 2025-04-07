package com.example.horse_racing.Models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UserWallet {
    private int id;
    private String email;
    private BigDecimal wallet;
    private Timestamp createAt;

    public UserWallet() {
    }

    public UserWallet(int id, String email, BigDecimal wallet, Timestamp createAt) {
        this.id = id;
        this.email = email;
        this.wallet = wallet;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
