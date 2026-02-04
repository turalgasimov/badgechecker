package com.badgechecker.model;

public class BadgeResult {
    private String badge;
    private boolean completed;

    public BadgeResult(String badge, boolean completed) {
        this.badge = badge;
        this.completed = completed;
    }

    public String getBadge() { return badge; }
    public boolean isCompleted() { return completed; }
}

