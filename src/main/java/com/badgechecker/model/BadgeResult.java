package com.badgechecker.model;

public class BadgeResult {
    private String badgeName;
    private boolean completed;

    // Default constructor
    public BadgeResult() {
    }

    // Constructor with parameters
    public BadgeResult(String badgeName, boolean completed) {
        this.badgeName = badgeName;
        this.completed = completed;
    }

    // Getters and Setters
    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "BadgeResult{" +
                "badgeName='" + badgeName + '\'' +
                ", completed=" + completed +
                '}';
    }
}