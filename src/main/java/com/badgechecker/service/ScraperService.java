package com.badgechecker.service;

import com.badgechecker.model.BadgeResult;
import com.badgechecker.model.UserBadgeReport;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperService {

    private static final Logger logger = LoggerFactory.getLogger(ScraperService.class);
    private static final String BASE_URL = "https://www.codecademy.com/users/";
    private static final int WAIT_TIMEOUT_SECONDS = 15;

    @Autowired
    private ApplicationContext applicationContext;

    public List<UserBadgeReport> checkBadges(List<String> usernames, List<String> badgeNames) {
        List<UserBadgeReport> reports = new ArrayList<>();

        for (String username : usernames) {
            UserBadgeReport report = checkUserBadges(username, badgeNames);
            reports.add(report);
        }

        return reports;
    }

    private UserBadgeReport checkUserBadges(String username, List<String> badgeNames) {
        WebDriver driver = null;
        UserBadgeReport report = new UserBadgeReport();
        report.setUsername(username);
        List<BadgeResult> results = new ArrayList<>();

        try {
            driver = applicationContext.getBean(WebDriver.class);
            String url = BASE_URL + username;
            
            logger.info("Checking badges for user: {}", username);
            driver.get(url);

            // Wait for page to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            
            // Wait for either the achievements section or error message
            try {
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid='profile-page']")),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".profile-header")),
                    ExpectedConditions.presenceOfElementLocated(By.tagName("body"))
                ));
            } catch (Exception e) {
                logger.warn("Timeout waiting for page load for user: {}", username);
            }

            // Give dynamic content time to load
            Thread.sleep(3000);

            // Get page source for debugging
            String pageSource = driver.getPageSource().toLowerCase();

            // Check each badge
            for (String badgeName : badgeNames) {
                boolean completed = checkBadgeCompletion(driver, pageSource, badgeName);
                results.add(new BadgeResult(badgeName, completed));
                logger.info("User: {} - Badge: {} - Completed: {}", username, badgeName, completed);
            }

        } catch (Exception e) {
            logger.error("Error checking badges for user: {}", username, e);
            // Add all badges as not completed on error
            for (String badgeName : badgeNames) {
                results.add(new BadgeResult(badgeName, false));
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }

        report.setBadges(results);
        return report;
    }

    private boolean checkBadgeCompletion(WebDriver driver, String pageSource, String badgeName) {
        try {
            // Strategy 1: Check if badge name appears in page source
            String badgeNameLower = badgeName.toLowerCase();
            
            // Look for various patterns that might indicate badge completion
            // This is a heuristic approach since Codecademy's structure may vary
            
            // Check in page source
            if (pageSource.contains(badgeNameLower)) {
                logger.debug("Badge name '{}' found in page source", badgeName);
                
                // Look for achievement/badge elements
                try {
                    List<WebElement> achievements = driver.findElements(By.cssSelector(
                        "[class*='achievement'], [class*='badge'], [class*='course'], [data-testid*='achievement'], [data-testid*='badge']"
                    ));
                    
                    for (WebElement achievement : achievements) {
                        String text = achievement.getText().toLowerCase();
                        if (text.contains(badgeNameLower)) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Error searching for badge elements: {}", e.getMessage());
                }
                
                // Fallback: if badge name is in page, assume it's completed
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            logger.error("Error checking badge: {}", badgeName, e);
            return false;
        }
    }
}