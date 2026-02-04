package com.badgechecker.model;

import java.util.List;

public class UserResult {
    private String username;
    private List<BadgeResult> badges;

    public UserResult(String username, List<BadgeResult> badges) {
        this.username = username;
        this.badges = badges;
    }

    public String getUsername() { return username; }
    public List<BadgeResult> getBadges() { return badges; }
}

