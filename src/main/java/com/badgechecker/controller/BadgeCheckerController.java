package com.badgechecker.controller;

import com.badgechecker.model.UserBadgeReport;
import com.badgechecker.service.ScraperService;
import com.badgechecker.service.CsvParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class BadgeCheckerController {

    private static final Logger logger = LoggerFactory.getLogger(BadgeCheckerController.class);

    @Autowired
    private CsvParserService csvParserService;

    @Autowired
    private ScraperService scraperService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/check")
    public String checkBadges(
            @RequestParam("usernamesFile") MultipartFile usernamesFile,
            @RequestParam("badgesFile") MultipartFile badgesFile,
            Model model) {

        try {
            // Validate files
            if (usernamesFile.isEmpty() || badgesFile.isEmpty()) {
                model.addAttribute("error", "Please upload both CSV files");
                return "index";
            }

            // Parse CSV files
            logger.info("Parsing usernames file...");
            List<String> usernames = csvParserService.parseUsernames(usernamesFile);
            
            logger.info("Parsing badges file...");
            List<String> badgeNames = csvParserService.parseBadgeNames(badgesFile);

            if (usernames.isEmpty() || badgeNames.isEmpty()) {
                model.addAttribute("error", "CSV files are empty or invalid");
                return "index";
            }

            logger.info("Found {} usernames and {} badges to check", usernames.size(), badgeNames.size());

            // Check badges
            List<UserBadgeReport> reports = scraperService.checkBadges(usernames, badgeNames);

            // Add results to model
            model.addAttribute("reports", reports);
            return "results";

        } catch (Exception e) {
            logger.error("Error processing request", e);
            model.addAttribute("error", "Error processing files: " + e.getMessage());
            return "index";
        }
    }
}