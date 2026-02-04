package com.badgechecker.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvParserService {

    public List<String> parseUsernames(MultipartFile file) throws IOException {
        List<String> usernames = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            for (CSVRecord record : csvParser) {
                // Assumes column name is "username" or takes first column
                String username = record.size() > 0 ? 
                    (record.isMapped("username") ? record.get("username") : record.get(0)) : null;
                
                if (username != null && !username.trim().isEmpty()) {
                    usernames.add(username.trim());
                }
            }
        }
        
        return usernames;
    }

    public List<String> parseBadgeNames(MultipartFile file) throws IOException {
        List<String> badgeNames = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            for (CSVRecord record : csvParser) {
                // Assumes column name is "badge" or "badgename" or takes first column
                String badgeName = record.size() > 0 ? 
                    (record.isMapped("badge") ? record.get("badge") : 
                     record.isMapped("badgename") ? record.get("badgename") : record.get(0)) : null;
                
                if (badgeName != null && !badgeName.trim().isEmpty()) {
                    badgeNames.add(badgeName.trim());
                }
            }
        }
        
        return badgeNames;
    }
}