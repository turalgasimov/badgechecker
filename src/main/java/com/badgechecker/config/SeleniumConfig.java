package com.badgechecker.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import io.github.bonigarcia.wdm.WebDriverManager;

@Configuration
public class SeleniumConfig {

    @Bean
    @Scope("prototype")
    public WebDriver webDriver() {
        // Download and setup ChromeDriver automatically
        WebDriverManager.chromedriver().avoidBrowserDetection().setup();

        ChromeOptions options = new ChromeOptions();
        options.setBinary("/usr/bin/google-chrome"); // Path on Ubuntu EC2
        options.addArguments(
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080");

        return new ChromeDriver(options);
    }
}