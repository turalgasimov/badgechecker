package com.badgechecker.model;

import java.util.List;

public class UserBadgeReport {
    private String username;
    private List<BadgeResult> badges;

    // Default constructor
    public UserBadgeReport() {
    }

    // Constructor with parameters
    public UserBadgeReport(String username, List<BadgeResult> badges) {
        this.username = username;
        this.badges = badges;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<BadgeResult> getBadges() {
        return badges;
    }

    public void setBadges(List<BadgeResult> badges) {
        this.badges = badges;
    }

    @Override
    public String toString() {
        return "UserBadgeReport{" +
                "username='" + username + '\'' +
                ", badges=" + badges +
                '}';
    }
}