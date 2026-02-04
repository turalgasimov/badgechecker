package com.badgechecker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import com.badgechecker.service.BadgeCheckerService;

@Controller
public class DashboardController {

    private final BadgeCheckerService service = new BadgeCheckerService();

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/run")
    public String run(
            @RequestParam("usernames") MultipartFile usernamesFile,
            @RequestParam("badges") MultipartFile badgesFile,
            Model model) {

        try {
            List<String> usernames = readCsv(usernamesFile);
            List<String> badges = readCsv(badgesFile);

            model.addAttribute("results", service.checkBadges(usernames, badges));
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "index";
        }
    }

    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return "OK";
    }

    private List<String> readCsv(MultipartFile file) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return br.lines()
                    .filter(l -> !l.isBlank())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV: " + e.getMessage(), e);
        }
    }

}

