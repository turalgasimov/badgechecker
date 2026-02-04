package com.badgechecker.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import com.badgechecker.model.*;

public class BadgeCheckerService {

    public List<UserResult> checkBadges(
            List<String> usernames,
            List<String> badgeNames) {

        // ---- Minimal change: setup geckodriver automatically ----
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // headless mode
        options.addArguments("--no-sandbox"); // required in containers
        options.addArguments("--disable-dev-shm-usage");// required in containers

        WebDriver driver = new ChromeDriver(options);
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
