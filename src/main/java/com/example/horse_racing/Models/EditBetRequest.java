package com.example.horse_racing.Models;

public class EditBetRequest {
    private int placedBetId;
    private String newState;

    public EditBetRequest() {}

    public EditBetRequest(int placedBetId, String newState) {
        this.placedBetId = placedBetId;
        this.newState = newState;
    }

    public int getPlacedBetId() {
        return placedBetId;
    }

    public void setPlacedBetId(int placedBetId) {
        this.placedBetId = placedBetId;
    }

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }
}
