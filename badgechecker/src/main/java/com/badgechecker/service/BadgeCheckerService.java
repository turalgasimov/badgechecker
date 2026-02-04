package com.badgechecker.service;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.*;
import com.badgechecker.model.*;

public class BadgeCheckerService {

    public List<UserResult> checkBadges(
            List<String> usernames,
            List<String> badgeNames) {

        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        List<UserResult> results = new ArrayList<>();

        try {
            for (String username : usernames) {
                driver.get(
                        "https://www.codecademy.com/users/"
                                + username + "/achievements");

                wait.until(
                        ExpectedConditions.presenceOfElementLocated(By.tagName("span")));

                List<WebElement> spans = driver.findElements(By.tagName("span"));
                List<BadgeResult> badgeResults = new ArrayList<>();

                for (String badge : badgeNames) {
                    boolean found = spans.stream()
                            .anyMatch(s -> s.getText().trim().equals(badge));

                    badgeResults.add(new BadgeResult(badge, found));
                }

                results.add(new UserResult(username, badgeResults));
            }
        } finally {
            driver.quit();
        }

        return results;
    }
}
