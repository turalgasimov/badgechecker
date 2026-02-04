package com.badgechecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.badgechecker")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
