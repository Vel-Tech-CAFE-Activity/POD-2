package com.files.upload.service;

import com.files.upload.Repository.FileDataRepository;
import com.files.upload.model.FileData;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FileDataService {
    Logger logger = org.slf4j.LoggerFactory.getLogger(FileDataService.class);
    private final FileDataRepository fileDataRepository;

    public FileDataService(FileDataRepository yourEntityRepository) {
        this.fileDataRepository = yourEntityRepository;
    }

    @Transactional
    public void store(List<Map<String, Object>> dataList, String moduleName, String fileName) throws NoSuchAlgorithmException,Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        Optional<List<FileData>> fileData1 = fileDataRepository.findByFileName(fileName);
        
        if(fileData1.isPresent()&&!fileData1.get().isEmpty()){
            throw new Exception("File already exists");
        }
        
        for (Map<String, Object> data : dataList) {
            String jsonData = gson.toJson(data);
            FileData entity = new FileData();
            entity.setJsonData(jsonData);
            entity.setName(moduleName);
            entity.setFileName(fileName);
            // Generate SHA-256 hash
            String hash = generateHash(jsonData);
            entity.setHash(hash);
            
            
            fileDataRepository.findByHash(hash).ifPresentOrElse(
                    (FileData fileData) -> {
                        logger.info("Record already exists");
                    },
                    () -> {
                        // Create new entity
                        // Update existing entity
                        fileDataRepository.save(entity);
                    }
            );

        }

    }

    private static String generateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // Convert byte array to hexadecimal string representation
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}

